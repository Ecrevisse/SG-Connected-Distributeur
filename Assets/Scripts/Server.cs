using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

using UnityEngine;

// State object for reading client data asynchronously
public class StateObject
{
    public Socket workSocket = null;
    public const int BufferSize = 1024;
    public byte[] receiveBuffer = new byte[BufferSize];
    public BytesBuffer stackBuffer = new BytesBuffer();
}

public class AsynchronousSocketListener
{
    // Thread signal.
    public ManualResetEvent allDone = new ManualResetEvent(false);

    private volatile bool _shouldStop;
    private string _ipAddress;
    private class ReceivedCode
    {
        public int code;
    }
    private ReceivedCode _receivedCode;

    public AsynchronousSocketListener()
    {
        _shouldStop = true;
        _ipAddress = "";
        _receivedCode = new ReceivedCode();
        _receivedCode.code = 0;
    }

    public string GetIpAddress()
    {
        lock (_ipAddress)
        {
            return _ipAddress;
        }
    }

    public int GetReceivedCode()
    {
        lock (_receivedCode)
        {
            int code = _receivedCode.code;
            _receivedCode.code = 0;
            return code;
        }
    }

    public void StopListening()
    {
        Debug.Log("Try to StopListening");
        if (_shouldStop == false)
        {
            Debug.Log("StopListening OK");
            _shouldStop = true;
            allDone.Set();
        }
    }

    public void StartListening()
    {
        if (_shouldStop == false)
            return;
        _shouldStop = false;

        // Establish the local endpoint for the socket.
        // The DNS name of the computer
        // running the listener is "host.contoso.com".
        IPHostEntry ipHostInfo = Dns.Resolve(Dns.GetHostName());
        IPAddress ipAddress = ipHostInfo.AddressList[0];
        IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 11000);

        lock (_ipAddress)
        {
            _ipAddress = ipAddress.ToString();
        }

        // Create a TCP/IP socket.
        Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

        // Bind the socket to the local endpoint and listen for incoming connections.
        try
        {
            listener.Bind(localEndPoint);
            listener.Listen(100);

            while (!_shouldStop)
            {
                // Set the event to nonsignaled state.
                allDone.Reset();

                // Start an asynchronous socket to listen for connections.
                Debug.Log("Waiting for a connection...");
                listener.BeginAccept(new AsyncCallback(AcceptCallback), listener);

                // Wait until a connection is made before continuing.
                allDone.WaitOne();
            }

        }
        catch (Exception e)
        {
            Debug.Log(e.ToString());
        }

        Debug.Log("Stop server");
    }

    public void AcceptCallback(IAsyncResult ar)
    {
        // Signal the main thread to continue.
        allDone.Set();

        // Get the socket that handles the client request.
        Socket listener = (Socket)ar.AsyncState;
        Socket handler = listener.EndAccept(ar);

        // Create the state object.
        StateObject state = new StateObject();
        state.workSocket = handler;
        handler.BeginReceive(state.receiveBuffer, 0, StateObject.BufferSize, 0, new AsyncCallback(ReadCallback), state);
    }

    public void ReadCallback(IAsyncResult ar)
    {
        String content = String.Empty;

        // Retrieve the state object and the handler socket
        // from the asynchronous state object.
        StateObject state = (StateObject)ar.AsyncState;
        Socket handler = state.workSocket;

        // Read data from the client socket. 
        int bytesRead = handler.EndReceive(ar);

        if (bytesRead > 0)
        {
            // There  might be more data, so store the data received so far.
            state.stackBuffer.Write(state.receiveBuffer, 0, bytesRead);

            long pos = state.stackBuffer.Position;
            uint sizeHandled = HandlePacket(state);
            state.stackBuffer.Position = pos;

            if (sizeHandled > 0)
            {
                BytesBuffer newBuf = new BytesBuffer();
                newBuf.Write(state.stackBuffer.GetBuffer(), (int)sizeHandled, (int)state.stackBuffer.Length - (int)sizeHandled);
                state.stackBuffer = newBuf;
            }

            handler.BeginReceive(state.receiveBuffer, 0, StateObject.BufferSize, 0, new AsyncCallback(ReadCallback), state);
        }
        //else
            //Disconnect
    }

    private void Send(Socket handler, String data)
    {
        // Convert the string data to byte data using ASCII encoding.
        byte[] byteData = Encoding.ASCII.GetBytes(data);

        // Begin sending the data to the remote device.
        handler.BeginSend(byteData, 0, byteData.Length, 0,
            new AsyncCallback(SendCallback), handler);
    }

    private void SendCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the socket from the state object.
            Socket handler = (Socket)ar.AsyncState;

            // Complete sending the data to the remote device.
            int bytesSent = handler.EndSend(ar);
            Debug.Log(string.Format("Sent {0} bytes to client.", bytesSent));

            handler.Shutdown(SocketShutdown.Both);
            handler.Close();

        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    //----------------------------------------------------
    //------------------HANDLE PACKET--------------------
    //----------------------------------------------------

    private uint HandlePacket(StateObject state)
    {
        uint totalLength = 0;
        int length;

        state.stackBuffer.Position = 0;
        try
        {
            while (state.stackBuffer.Position < state.stackBuffer.Length)
            {
                length = state.stackBuffer.ReadVarInt();
                if (length > state.stackBuffer.Length - state.stackBuffer.Position)
                    return totalLength;
                BytesBuffer packetToHandle = new BytesBuffer();
                packetToHandle.Write(state.stackBuffer.GetBuffer(), (int)state.stackBuffer.Position, length);
                packetToHandle.Position = 0;
                int type = packetToHandle.ReadVarInt();
                if (type == 0x00)
                    HandleReceiveCode(packetToHandle);
                state.stackBuffer.Position += length;
                totalLength = (uint)state.stackBuffer.Position;
            }
        }
        catch (System.ArgumentException e)
        {
            System.Diagnostics.Debug.WriteLine(e.ToString());
            return 0;
        }
        return totalLength;
    }

    private void HandleReceiveCode(BytesBuffer buffer)
    {
        int code = buffer.ReadInt();
        lock (_receivedCode)
        {
            _receivedCode.code = code;
        }
    }
}