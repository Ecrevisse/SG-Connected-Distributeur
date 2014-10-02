using UnityEngine;
using System.Collections;
using System.Threading;

public class ButtonStartServer : MonoBehaviour
{
    public GUIText IpText;

    private AsynchronousSocketListener _server;
    private Thread _thread;
    private bool _ipSetted;

    void Clicked()
    {
        Debug.Log("starting server...");
        _server = new AsynchronousSocketListener();
        _thread = new Thread(_server.StartListening);
        _thread.Start();
        _ipSetted = false;
    }

    void Update()
    {
        if (_server != null && _ipSetted == false)
        {
            string ip = _server.GetIpAddress();
            if (ip != "")
            {
                IpText.text += ip;
                _ipSetted = true;
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
