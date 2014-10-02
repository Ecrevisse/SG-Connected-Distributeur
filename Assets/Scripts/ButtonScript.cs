using UnityEngine;
using System.Collections;

public class ButtonScript : MonoBehaviour {

    public ScreenManagerScript manager;
    public int value;
	// Use this for initialization
	void Start () {
        
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    void Clicked()
    {
        manager.buttonNum(value);
    }
}
