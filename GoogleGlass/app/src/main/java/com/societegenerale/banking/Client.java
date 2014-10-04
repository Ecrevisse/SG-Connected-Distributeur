package com.societegenerale.banking;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Raveh on 04/10/2014.
 */
public class Client extends AsyncTask<Void, Integer, Void>
{
    public interface ClientCallbacks
    {
        void callbackReceiveUniqueId(int uniqueId);
        void callbackReceiveIdOk();
        void callbackReceiveAmountOk();
    }

    String _dstAddress;
    int _dstPort;

    private ClientCallbacks _callbacks;
    private Socket _socket;
    private DatagramSocket _broadcastSocket;
    private DatagramSocket _receiveBroadcastSocket;
    private CustomByteBuffer _sendBuffer;
    private CustomByteBuffer _receiveBuffer;
    private volatile boolean _shouldStop;
    private Context _context;

    Client(ClientCallbacks callbacks, Context context)
    {
        //dstAddress = "10.12.20.190";
        _dstAddress = "";
        _dstPort = 11000;
        _shouldStop = false;
        _sendBuffer = new CustomByteBuffer();
        _receiveBuffer = new CustomByteBuffer();
        _callbacks = callbacks;
        _context = context;
    }

    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager)_context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    @Override
    protected Void doInBackground(Void... arg0)
    {
        _socket = null;

        try
        {
            _broadcastSocket = new DatagramSocket(15000);
            _broadcastSocket.setBroadcast(true);

            _receiveBroadcastSocket = new DatagramSocket(15001);
            _broadcastSocket.setSoTimeout(1000);

            String sgBroadcastString = "SGCONNECTEDHACKBROADCAST";

            while (_shouldStop == false && _dstAddress == "")
            {
                DatagramPacket packet = new DatagramPacket(sgBroadcastString.getBytes(), sgBroadcastString.length(),
                        getBroadcastAddress(), 15000);
                _broadcastSocket.send(packet);
                byte[] recBuf = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(recBuf, recBuf.length);
                try
                {
                    _receiveBroadcastSocket.receive(receivedPacket);
                }
                catch (SocketTimeoutException e)
                {
                    // resend
                    continue;
                }
                String message = new String(receivedPacket.getData()).trim();
                if (message.equals(sgBroadcastString))
                {
                    _dstAddress = receivedPacket.getAddress().getHostAddress();
                }
            }
            _broadcastSocket.close();
            _broadcastSocket = null;
            _receiveBroadcastSocket.close();
            _broadcastSocket = null;
            if (_shouldStop == true)
                return null;

            _socket = new Socket(_dstAddress, _dstPort);

            byte[] buffer = new byte[1024];
            int bytesRead;
            InputStream inputStream = _socket.getInputStream();

            while (_shouldStop == false)
            {
                if (_sendBuffer.GetLength() != 0)
                {
                    OutputStream outputStream = _socket.getOutputStream();
                    outputStream.write(_sendBuffer.GetBuffer());
                    _sendBuffer = new CustomByteBuffer();
                }

                if (inputStream.available() != 0)
                {
                    bytesRead = inputStream.read(buffer);
                    _receiveBuffer.Write(buffer, 0, bytesRead);
                    int sizeHandled = HandlePacket();
                    //_receiveBuffer.Reset();
                    //if (sizeHandled > 0)
                    //{
                    //    CustomByteBuffer newBuf = new CustomByteBuffer();
                    //    newBuf.Write(_receiveBuffer.GetBuffer(), sizeHandled, _receiveBuffer.GetLength() - sizeHandled);
                    //    _receiveBuffer = newBuf;
                    //}
                }
            }
        }
        catch (UnknownHostException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Client", "Client.SendCode() — UnknownHostException: " + e.toString());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Client", "Client.SendCode() — IOException: " + e.toString());
        }
        finally
        {
            if(_socket != null)
            {
                try
                {
                    _socket.close();
                    _socket = null;
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(_broadcastSocket != null)
            {
                _broadcastSocket.close();
                _broadcastSocket = null;
            }
            if(_receiveBroadcastSocket != null)
            {
                _receiveBroadcastSocket.close();
                _receiveBroadcastSocket = null;
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... data)
    {
        if (_callbacks != null)
        {
            if (data[0] == 0)
            {
                _callbacks.callbackReceiveUniqueId(data[1]);
            }
            else if (data[0] == 1)
            {
                _callbacks.callbackReceiveIdOk();
            }
            else if (data[0] == 2)
            {
                _callbacks.callbackReceiveAmountOk();
            }
        }
    }

    @Override
    protected void onPostExecute(Void result)
    {
        /*if (response == "Connected")
            setContentView(R.layout.activity_code);
        else
            textResponse.setText(response);*/
        super.onPostExecute(result);
    }

    private int HandlePacket()
    {
        int totalLength = 0;

        try
        {
            while (_receiveBuffer.AvailableToRead() != 0)
            {
                int len = _receiveBuffer.ReadVarInt();
                if (len > _receiveBuffer.AvailableToRead())
                    return 0;
                CustomByteBuffer packetToHandle = new CustomByteBuffer();
                packetToHandle.Write(_receiveBuffer.GetBuffer(), _receiveBuffer.GetLastVarIntSize(), len);
                int lenToSkip = len + _receiveBuffer.GetLastVarIntSize();
                totalLength += lenToSkip;
                int type = packetToHandle.ReadVarInt();
                if (type == 0x10)
                    HandleReceiveUniqueId(packetToHandle);
                else if (type == 0x11)
                    HandleReceiveIdOk(packetToHandle);
                else if (type == 0x12)
                    HandleReceiveAmountOk(packetToHandle);
                _receiveBuffer.Skip(lenToSkip);
            }
        }
        catch (RuntimeException e)
        {
            return 0;
        }
        return totalLength;
    }

    public void HandleReceiveUniqueId(CustomByteBuffer packet)
    {
        int uniqueID = packet.ReadInt();
        publishProgress(0, uniqueID);
    }

    public void HandleReceiveIdOk(CustomByteBuffer packet)
    {
        publishProgress(1);
    }

    public void HandleReceiveAmountOk(CustomByteBuffer packet)
    {
        publishProgress(2);
    }

    public void Stop()
    {
        _shouldStop = true;
    }

    public void SendCode(int code)
    {
        CustomByteBuffer cbf = new CustomByteBuffer();
        cbf.WriteVarInt(0);
        cbf.WriteInt(code);

        CustomByteBuffer toSend = new CustomByteBuffer();
        toSend.WriteVarInt(cbf.GetLength());
        toSend.Write(cbf.GetBuffer());
        _sendBuffer.Write(toSend.GetBuffer());
    }
}
