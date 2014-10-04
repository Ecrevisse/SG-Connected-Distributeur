﻿using UnityEngine;
using System.Collections;
using System.Threading;
using System.Timers;
using System.Net.Sockets;
using System.Net;
using System.Text;

public class ButtonStartServer : MonoBehaviour
{
    public GUIText IpText;
    public GUIText CodeText;
    public GUIText UdpText;
    public ScreenManagerScript manager;

    public AsynchronousSocketListener _server;
    private Thread _thread;
    private bool _ipSetted;
    private bool _codeSetted;

    const int PORT_NUMBER = 15000;

    void Start()
    {
        Debug.Log("starting server...");
        _server = new AsynchronousSocketListener();
        _thread = new Thread(_server.StartListening);
        _thread.Start();
        _ipSetted = false;
        _codeSetted = false;
        _server.StartUdpListening();
    }

    void Update()
    {
        if (_server != null)
        {
            if (_ipSetted == false)
            {
                string ip = _server.GetIpAddress();
                if (ip != "")
                {
                    IpText.text += ip;
                    _ipSetted = true;
                }
            }

            if (_codeSetted == false)
            {
                int code = _server.GetReceivedCode();
                if (code != 0)
                {
                    CodeText.text += code.ToString();
                    _codeSetted = true;
                    if (manager != null)
                        manager.setCodeGG(code);
                }
            }
            if (UdpText != null)
                UdpText.text += _server.GetUdpMessage();
        }
    }

    void OnApplicationQuit()
    {
        if (_server != null)
        {
            _server.StopListening();
            _thread.Join();
        }
    }

    void GenerateUniqueCodeGG()
    {
        //after
    }
}
