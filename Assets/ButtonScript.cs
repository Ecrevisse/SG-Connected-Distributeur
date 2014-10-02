using UnityEngine;
using System.Collections;

public class ButtonScript : MonoBehaviour {

    public GUIText pop;
    public string text;
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    void Clicked()
    {
        pop.text = text;
    }
}
