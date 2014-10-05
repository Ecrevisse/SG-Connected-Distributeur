using UnityEngine;
using System.Collections;
using System.Threading;
using System.Timers;
using System.Net.Sockets;
using System.Net;
using System.Text;

public class ButtonStartServer : MonoBehaviour
{
    public ScreenManagerScript manager;

    public AsynchronousSocketListener _server;
    private Thread _thread;

    const int PORT_NUMBER = 15000;

    void Start()
    {
        Debug.Log("starting server...");
        _server = new AsynchronousSocketListener();
        _thread = new Thread(_server.StartListening);
        _thread.Start();
        _server.StartUdpListening();
    }

    void Update()
    {
        if (_server != null)
        {
            int code = _server.GetReceivedCode();
            if (code != -1)
            {
                if (manager != null)
                    manager.setCodeGG(code);
            }
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
}
