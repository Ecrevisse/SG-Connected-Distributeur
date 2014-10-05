using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

using UnityEngine;
using System.Collections.Generic;

// State object for reading client data asynchronously
public class StateObject
{
    public Socket workSocket = null;
    public const int BufferSize = 1024;
    public byte[] receiveBuffer = new byte[BufferSize];
    public BytesBuffer stackBuffer = new BytesBuffer();
    public bool connected;
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
    private string _udpMessage;
    private List<StateObject> _clientList;

    public AsynchronousSocketListener()
    {
        _shouldStop = true;
        _ipAddress = "";
        _receivedCode = new ReceivedCode();
        _receivedCode.code = -1;
        _udpMessage = "";
        _clientList = new List<StateObject>();
    }

    public string GetUdpMessage()
    {
        lock (_udpMessage)
        {
            return _udpMessage;
        }
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
            _receivedCode.code = -1;
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
        Debug.Log(_ipAddress);
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
        state.connected = true;
        Debug.Log("New client");
        lock (_clientList)
        {
            _clientList.Add(state);
        }
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
        Debug.Log("Receive : " + bytesRead);
        if (bytesRead > 0)
        {
            // There  might be more data, so store the data received so far.
            state.stackBuffer.Write(state.receiveBuffer, 0, bytesRead);

            long pos = state.stackBuffer.Position;
            uint sizeHandled = HandlePacket(state, handler);
            state.stackBuffer.Position = pos;

            if (sizeHandled > 0)
            {
                BytesBuffer newBuf = new BytesBuffer();
                newBuf.Write(state.stackBuffer.GetBuffer(), (int)sizeHandled, (int)state.stackBuffer.Length - (int)sizeHandled);
                state.stackBuffer = newBuf;
            }

            handler.BeginReceive(state.receiveBuffer, 0, StateObject.BufferSize, 0, new AsyncCallback(ReadCallback), state);
        }
        else
        {
            Debug.Log("disconnect client");
            lock (_clientList)
            {
                state.connected = false;
                _clientList.Remove(state);
            }
            handler.Shutdown(SocketShutdown.Both);
            handler.Close();
        }
    }

    private void Send(Socket handler, BytesBuffer packet)
    {
        // Begin sending the data to the remote device.
        handler.BeginSend(packet.GetBuffer(), 0, (int)packet.Length, 0,
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
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    //----------------------------------------------------
    //------------------HANDLE PACKET--------------------
    //----------------------------------------------------

    private uint HandlePacket(StateObject state, Socket handler)
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
                    HandleReceiveCode(packetToHandle, handler);
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

    private void HandleReceiveCode(BytesBuffer buffer, Socket handler)
    {
        int code = buffer.ReadInt();
        lock (_receivedCode)
        {
            _receivedCode.code = code;
            Debug.Log("code received : " + code);
        }
    }

    //----------------------------------------------------
    //--------------------SEND PACKET--------------------
    //----------------------------------------------------

    public StateObject SendUniqueId(int uniqueId)
    {
        if (_clientList.Count != 0)
        {
            StateObject client = _clientList[0];

            BytesBuffer tmp = new BytesBuffer();
            tmp.WriteVarInt(0x10);
            tmp.WriteInt(uniqueId);

            BytesBuffer toSend = new BytesBuffer();
            toSend.WriteVarInt((int)tmp.Length);
            toSend.Write(tmp.GetBuffer(), 0, (int)tmp.Length);
            Debug.Log("Send unique Id");
            Send(client.workSocket, toSend);
            return client;
        }
        return null;
    }

    public void SendIdOk(StateObject client)
    {
        BytesBuffer tmp = new BytesBuffer();
        tmp.WriteVarInt(0x11);

        BytesBuffer toSend = new BytesBuffer();
        toSend.WriteVarInt((int)tmp.Length);
        toSend.Write(tmp.GetBuffer(), 0, (int)tmp.Length);
        Debug.Log("Send id ok");
        Send(client.workSocket, toSend);
    }

    public void SendAmountOk(StateObject client)
    {
        BytesBuffer tmp = new BytesBuffer();
        tmp.WriteVarInt(0x12);

        BytesBuffer toSend = new BytesBuffer();
        toSend.WriteVarInt((int)tmp.Length);
        toSend.Write(tmp.GetBuffer(), 0, (int)tmp.Length);
        Debug.Log("Send amount ok");
        Send(client.workSocket, toSend);
    }

    public void SendPinStatus(StateObject client, bool statut)
    {
        BytesBuffer tmp = new BytesBuffer();
        tmp.WriteVarInt(0x13);
        if (statut)
            tmp.WriteInt(1);
        else
            tmp.WriteInt(0);

        BytesBuffer toSend = new BytesBuffer();
        toSend.WriteVarInt((int)tmp.Length);
        toSend.Write(tmp.GetBuffer(), 0, (int)tmp.Length);
        if (statut)
            Debug.Log("Send pin ok");
        else
            Debug.Log("Send pin false");
        Send(client.workSocket, toSend);
    }

    public void SendTransactionStatus(StateObject client, bool statut)
    {
        BytesBuffer tmp = new BytesBuffer();
        tmp.WriteVarInt(0x14);
        if (statut)
            tmp.WriteInt(1);
        else
            tmp.WriteInt(0);

        BytesBuffer toSend = new BytesBuffer();
        toSend.WriteVarInt((int)tmp.Length);
        toSend.Write(tmp.GetBuffer(), 0, (int)tmp.Length);
        if (statut)
            Debug.Log("Send transaction completed");
        else
            Debug.Log("Send transaction canceled");
        Send(client.workSocket, toSend);
    }

    public void EndTransaction(StateObject client)
    {
        Debug.Log("End transaction");
        lock (_clientList)
        {
            client.connected = false;
            _clientList.Remove(client);
        }
        client.workSocket.Shutdown(SocketShutdown.Both);
        client.workSocket.Close();
    }

    //----------------------------------------------------
    //--------------------    UDP    --------------------
    //----------------------------------------------------

    private readonly UdpClient udp = new UdpClient(15000);
    IAsyncResult ar_ = null;

    public void StartUdpListening()
    {
        ar_ = udp.BeginReceive(Receive, new object());
    }
    private void Receive(IAsyncResult ar)
    {
        IPEndPoint ip = new IPEndPoint(IPAddress.Any, 15000);
        byte[] bytes = udp.EndReceive(ar, ref ip);
        string message = Encoding.ASCII.GetString(bytes);
        Debug.Log(string.Format("From {0} received: {1} ", ip.Address.ToString(), message));
        _udpMessage = string.Format("From {0} received: {1} ", ip.Address.ToString(), message);
        //Sending reply
        if (message == "SGCONNECTEDHACKBROADCAST" && _ipAddress != "")
        {
            UdpClient client = new UdpClient();
            IPEndPoint ipClient = new IPEndPoint(ip.Address, 15001);
            string messageToSend = "SGCONNECTEDHACKBROADCAST";
            Debug.Log(messageToSend);
            byte[] bytesClient = Encoding.ASCII.GetBytes(messageToSend);
            client.Send(bytesClient, bytesClient.Length, ipClient);
            client.Close();
        }
        if (_shouldStop == false)
            StartUdpListening();
    }
}