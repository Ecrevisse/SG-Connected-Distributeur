using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class ScreenManagerScript : MonoBehaviour
{

    public enum PageSelec
    {
        E_HOME,
        E_CHOOSE_QUANTITY,
        E_CHOOSE_QUANTITY_BY_KEYBOARD,
        E_ENTER_CODE,
        E_GOOD_CODE,
        E_WRONG_CODE
    };

    private List<List<string>> strings = new List<List<string>>();
    private PageSelec currentPage;
    private int currentNumberTyped;
    private int money;

    public TextMesh text0;
    public TextMesh text1;
    public TextMesh text2;
    public TextMesh text3;
    public TextMesh text4;
    public TextMesh text5;
    public TextMesh text6;
    public TextMesh text7;

    public TextMesh InputText;

	// Use this for initialization
	void Start () 
    {
        currentPage = PageSelec.E_HOME;
        currentNumberTyped = 0;
        money = 0;

	    strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());

        strings[(int)PageSelec.E_HOME].Add("Normal");
        strings[(int)PageSelec.E_HOME].Add("Google Glass");
        strings[(int)PageSelec.E_HOME].Add("");
        strings[(int)PageSelec.E_HOME].Add("");
        strings[(int)PageSelec.E_HOME].Add("");
        strings[(int)PageSelec.E_HOME].Add("");
        strings[(int)PageSelec.E_HOME].Add("");
        strings[(int)PageSelec.E_HOME].Add("");

        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("20€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("40€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("60€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("80€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("100€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("150€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("200€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("Autre Montant");

        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("Entrez le montant :");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");

        strings[(int)PageSelec.E_ENTER_CODE].Add("Entrez le code de carte :");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");

        strings[(int)PageSelec.E_GOOD_CODE].Add("Patientez s'il vous plait,\nnous preparons vos billets...");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");

        strings[(int)PageSelec.E_WRONG_CODE].Add("Mauvais code, veuillez réessayer");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");

        changeTo(currentPage);
	}
	
	// Update is called once per frame
	void Update () 
    {
	    
	}

    public void changeTo(PageSelec page)
    {
        currentPage = page;

        text0.text = strings[(int)currentPage][0];
        text1.text = strings[(int)currentPage][1];
        text2.text = strings[(int)currentPage][2];
        text3.text = strings[(int)currentPage][3];
        text4.text = strings[(int)currentPage][4];
        text5.text = strings[(int)currentPage][5];
        text6.text = strings[(int)currentPage][6];
        text7.text = strings[(int)currentPage][7];
        if (currentPage == PageSelec.E_GOOD_CODE)
        {
            currentNumberTyped = money;
            InputText.text = "" + currentNumberTyped;
        }
    }

    public void buttonSelectClicked(int num)
    {
        bool isPageChanged = false;

        if (currentPage == PageSelec.E_HOME)
        {
            if (num == 0)
            {
                currentPage = PageSelec.E_CHOOSE_QUANTITY;
                currentNumberTyped = 0;
                isPageChanged = true;
            }
        }
        else if (currentPage == PageSelec.E_CHOOSE_QUANTITY)
        {
            if (num != 7)
            {
                currentPage = PageSelec.E_ENTER_CODE;
                currentNumberTyped = 0;
                money = int.Parse(strings[(int)PageSelec.E_CHOOSE_QUANTITY][num].Remove(strings[(int)PageSelec.E_CHOOSE_QUANTITY][num].Length - 1));
                isPageChanged = true;
            }
            else
            {
                currentPage = PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD;
                currentNumberTyped = 0;
                isPageChanged = true;
            }
        }

        if (isPageChanged == true)
            changeTo(currentPage);
    }

    public void buttonNum(int num)
    {
        bool isPageChanged = false;

        if (num >= 0 && (currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD || currentPage == PageSelec.E_ENTER_CODE))
        {
            currentNumberTyped = currentNumberTyped * 10 + num;
        }
        else if (num == -1 && (currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD || currentPage == PageSelec.E_ENTER_CODE))
        {
            currentNumberTyped /= 10;
        }
        else if (num == -2)
        {
            if (currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD)
            {
                if (currentNumberTyped > 10 && currentNumberTyped % 10 == 0)
                {
                    money = currentNumberTyped;
                    currentPage = PageSelec.E_ENTER_CODE;
                    currentNumberTyped = 0;
                    isPageChanged = true;
                }
            }
            else if (currentPage == PageSelec.E_ENTER_CODE)
            {
                if (currentNumberTyped == 1234)
                {
                    currentPage = PageSelec.E_GOOD_CODE;
                    currentNumberTyped = 0;
                    isPageChanged = true;

                }
                else
                {
                    currentPage = PageSelec.E_WRONG_CODE;
                    currentNumberTyped = 0;
                    isPageChanged = true;
                }
            }
        }
        else
        {
            currentPage = PageSelec.E_HOME;
            currentNumberTyped = 0;
            isPageChanged = true;

        }

        if (currentNumberTyped > 0)
            InputText.text = "" + currentNumberTyped;
        else
            InputText.text = "";

        if (isPageChanged == true)
            changeTo(currentPage);
    }
}
