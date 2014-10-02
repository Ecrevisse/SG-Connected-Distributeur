using UnityEngine;
using System.Collections;
using System.Threading;

public class ButtonStartServer : MonoBehaviour
{
    private AsynchronousSocketListener  _server;
    private Thread                      _thread;

    void Clicked()
    {
        Debug.Log("starting server...");
        _server = new AsynchronousSocketListener();
        _thread = new Thread(_server.StartListening);
        _thread.Start();
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
