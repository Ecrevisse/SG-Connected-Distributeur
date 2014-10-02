using UnityEngine;
using System.Collections;

public class TactilTest : MonoBehaviour 
{

    public GUIText pop;
    public RuntimePlatform platform;

	// Use this for initialization
	void Start () 
    {
	    platform = Application.platform;
	}
	
	// Update is called once per frame

    void Update()
    {
        if(platform == RuntimePlatform.Android)
        {
            if(Input.touchCount > 0) 
            {
                if(Input.GetTouch(0).phase == TouchPhase.Began)
                {
                    //pop.text = "Position: " + Input.GetTouch(0).position;
                    checkTouch(Input.GetTouch(0).position);
                }
            }
        }
        else if(platform == RuntimePlatform.WindowsEditor)
        {
            if(Input.GetMouseButtonDown(0)) 
            {
                //pop.text = "Position: " + Input.mousePosition;
                checkTouch(Input.mousePosition);
            }
        }
    }
 
    void checkTouch(Vector2 pos)
    {
        Vector3 wp          = Camera.main.ScreenToWorldPoint(pos);
        Vector2 touchPos    = new Vector2(wp.x, wp.y);
        Collider2D hit      = Physics2D.OverlapPoint(touchPos);

        if (hit)
        {
            Debug.Log(hit.transform.gameObject.name);
            hit.transform.gameObject.SendMessage("Clicked", 0, SendMessageOptions.DontRequireReceiver);
        }
        else
        {
            Debug.Log(pos);
        }
    }
}
