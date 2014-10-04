using UnityEngine;
using System.Collections;
using System.Threading;

public class ButtonStartServer : MonoBehaviour
{
    public GUIText IpText;
    public GUIText CodeText;
    public ScreenManagerScript manager;

    private AsynchronousSocketListener _server;
    private Thread _thread;
    private bool _ipSetted;
    private bool _codeSetted;

    //DEBUG
    private static int _uniqueId;

    void Start()
    {
        Debug.Log("starting server...");
        _server = new AsynchronousSocketListener();
        _thread = new Thread(_server.StartListening);
        _thread.Start();
        _ipSetted = false;
        _codeSetted = false;
        //DEBUG
        _uniqueId = 0;
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
            else
            {
                //DEBUG
                _server.SendUniqueId(_uniqueId++);
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

    void GenerateUniqueCodeGG()
    {
        //after
    }
}
