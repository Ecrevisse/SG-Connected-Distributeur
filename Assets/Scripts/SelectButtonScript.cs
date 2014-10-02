using UnityEngine;
using System.Collections;

public class SelectButtonScript : MonoBehaviour {

    public ScreenManagerScript manager;
    public int id;
	// Use this for initialization
	void Start () {
	    
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    void Clicked()
    {
        manager.buttonSelectClicked(id);
    }
}
