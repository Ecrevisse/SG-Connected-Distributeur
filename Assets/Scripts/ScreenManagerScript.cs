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
        E_WRONG_CODE,
        E_ENTER_UNIQUE_CODE_GG,
        E_CHOOSE_QUANTITY_GG,
        E_CHOOSE_QUANTITY_BY_KEYBOARD_GG,
        E_ENTER_AMOUNT_GG,
        E_ENTER_CODE_GG
    };

    private List<List<string>> strings = new List<List<string>>();
    private PageSelec currentPage;
    private int currentNumberTyped;
    private int money;
    private int GoodCode = 1234;
    private int UniqueCodeGG = 123456;
    private StateObject client;
    private int cptToAbort = 0;
    private float timeBeforeReturnHome;

    public ButtonStartServer server;
    public TextMesh text0;
    public TextMesh text1;
    public TextMesh text2;
    public TextMesh text3;
    public TextMesh text4;
    public TextMesh text5;
    public TextMesh text6;
    public TextMesh text7;
	public TextMesh text8;
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
        strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());
        strings.Add(new List<string>());
		strings.Add(new List<string>());

		strings[(int)PageSelec.E_HOME].Add("RETRAIT PAR\nCARTE");
		strings[(int)PageSelec.E_HOME].Add("RETRAIT VIA\nGOOGLE GLASS");
        strings[(int)PageSelec.E_HOME].Add("");
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
        strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("SAISIE DE\nMONTANT");
		strings[(int)PageSelec.E_CHOOSE_QUANTITY].Add("");

        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD].Add("ENTREZ LE MONTANT :");

        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("");
        strings[(int)PageSelec.E_ENTER_CODE].Add("ENTREZ VOTRE CODE :");
		
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
        strings[(int)PageSelec.E_GOOD_CODE].Add("");
		strings[(int)PageSelec.E_GOOD_CODE].Add("");
		strings[(int)PageSelec.E_GOOD_CODE].Add("PATIENTEZ S'IL VOUS PLAIT,\nNOUS PREPARONS VOS BILLETS.");

		
		strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
        strings[(int)PageSelec.E_WRONG_CODE].Add("");
		strings [(int)PageSelec.E_WRONG_CODE].Add ("");
		strings[(int)PageSelec.E_WRONG_CODE].Add("TRANSACTION ANNULEE");

        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
		strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
		strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_UNIQUE_CODE_GG].Add("ENTREZ LE CODE\nD'AUTHENTIFICATION :");

        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("20€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("40€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("60€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("80€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("100€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("150€");
        strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("200€");
		strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("AUTRE MONTANT");
		strings[(int)PageSelec.E_CHOOSE_QUANTITY_GG].Add("");

        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
		strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("");
        strings[(int)PageSelec.E_ENTER_AMOUNT_GG].Add("ENTREZ LE MONTANT :");

		
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
		strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
		strings[(int)PageSelec.E_ENTER_CODE_GG].Add("");
        strings[(int)PageSelec.E_ENTER_CODE_GG].Add("EN ATTENTE DU CODE...");

        changeTo(currentPage);

        timeBeforeReturnHome = 0.0f;
	}

	// Update is called once per frame
	void Update () 
    {
        //if (client == null)
        //    client = server._server.RequestClient();
        StateObject tmpClient = server._server.GetClientToHandle();
        if (tmpClient != null)
            client = tmpClient;
        if ((currentPage == PageSelec.E_HOME || currentPage == PageSelec.E_ENTER_UNIQUE_CODE_GG)
            && client != null && client.requestUniqueId)
        {
            UniqueCodeGG = Random.Range(100000, 999999);
            //UniqueCodeGG = Random.Range(0, 9);
            server._server.SendUniqueId(UniqueCodeGG, client);
        }
        if (currentPage == PageSelec.E_GOOD_CODE || currentPage == PageSelec.E_WRONG_CODE)
        {
            if (timeBeforeReturnHome == 0.0f && client != null)
                server._server.SendTransactionStatus(client, (PageSelec.E_GOOD_CODE == currentPage) ? true : false);
            timeBeforeReturnHome += Time.deltaTime;
        }
        if (timeBeforeReturnHome > 3.0f)
        {
            currentPage = PageSelec.E_HOME;
            currentNumberTyped = 0;
            changeTo(currentPage);
            timeBeforeReturnHome = 0.0f;
        }
        if (client != null && client.connected == false &&
            currentPage != PageSelec.E_GOOD_CODE && currentPage != PageSelec.E_WRONG_CODE)
        {
            currentPage = PageSelec.E_HOME;
            currentNumberTyped = 0;
            changeTo(currentPage);
        }
        if (client != null && client.connected == false)
            client = null;
	}

    public void changeTo(PageSelec page)
    {
        currentPage = page;
        cptToAbort = 0;
        text0.text = strings[(int)currentPage][0];
        text1.text = strings[(int)currentPage][1];
        text2.text = strings[(int)currentPage][2];
        text3.text = strings[(int)currentPage][3];
        text4.text = strings[(int)currentPage][4];
        text5.text = strings[(int)currentPage][5];
        text6.text = strings[(int)currentPage][6];
        text7.text = strings[(int)currentPage][7];
		text8.text = strings[(int)currentPage][8];
        changeBack(text0);
        changeBack(text1);
        changeBack(text2);
        changeBack(text3);
        changeBack(text4);
        changeBack(text5);
        changeBack(text6);
        changeBack(text7);
		changeBack(text8);
    }

    private void changeBack(TextMesh text)
    {
        if (text.text.Length == 0)
            text.GetComponentInChildren<SpriteRenderer>().enabled = false;
        else
            text.GetComponentInChildren<SpriteRenderer>().enabled = true;
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
            else if (num == 1)
            {
                currentPage = PageSelec.E_ENTER_UNIQUE_CODE_GG;
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
        else if (currentPage == PageSelec.E_CHOOSE_QUANTITY_GG)
        {
            if (num != 7)
            {
                currentPage = PageSelec.E_ENTER_CODE_GG;
                currentNumberTyped = 0;
                money = int.Parse(strings[(int)PageSelec.E_CHOOSE_QUANTITY][num].Remove(strings[(int)PageSelec.E_CHOOSE_QUANTITY][num].Length - 1));
                isPageChanged = true;
                server._server.SendAmountOk(client);
            }
            else
            {
                currentPage = PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD_GG;
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

        if (num >= 0 && (currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD || currentPage == PageSelec.E_ENTER_CODE ||
                         currentPage == PageSelec.E_ENTER_UNIQUE_CODE_GG || currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD_GG))
        {
            currentNumberTyped = currentNumberTyped * 10 + num;
        }
        else if (num == -1 && (currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD || currentPage == PageSelec.E_ENTER_CODE ||
                               currentPage == PageSelec.E_ENTER_UNIQUE_CODE_GG || currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD_GG))
        {
            currentNumberTyped = 0;
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
            else if (currentPage == PageSelec.E_CHOOSE_QUANTITY_BY_KEYBOARD_GG)
            {
                if (currentNumberTyped > 10 && currentNumberTyped % 10 == 0)
                {
                    money = currentNumberTyped;
                    currentPage = PageSelec.E_ENTER_CODE_GG;
                    currentNumberTyped = 0;
                    isPageChanged = true;
                    server._server.SendAmountOk(client);
                }
            }
            else if (currentPage == PageSelec.E_ENTER_CODE)
            {
                if (currentNumberTyped == GoodCode)
                {
                    currentPage = PageSelec.E_GOOD_CODE;
                    currentNumberTyped = 0;
                    isPageChanged = true;
                    cptToAbort = 0;
                    Debug.Log("4" + cptToAbort);

                }
                else
                {
                    ++cptToAbort;
                    if (cptToAbort > 2)
                    {
                        currentPage = PageSelec.E_WRONG_CODE;
                        currentNumberTyped = 0;
                        isPageChanged = true;
                        cptToAbort = 0;
                        Debug.Log("5 " + cptToAbort);
                    }
                    else
                    {
                        Debug.Log("6 " + cptToAbort);
                        currentNumberTyped = 0;
                        text8.text = "MAUVAIS CODE, REESSAYEZ";
                        changeBack(text8);
                    }
                }
            }
            else if (currentPage == PageSelec.E_ENTER_UNIQUE_CODE_GG)
            {
                if (currentNumberTyped == UniqueCodeGG)
                {
                    currentPage = PageSelec.E_CHOOSE_QUANTITY_GG;
                    currentNumberTyped = 0;
                    isPageChanged = true;
                    server._server.SendIdOk(client);

                }
                else
                {
                    ++cptToAbort;
                    if (cptToAbort > 2)
                    {
                        currentPage = PageSelec.E_WRONG_CODE;
                        currentNumberTyped = 0;
                        isPageChanged = true;
                        cptToAbort = 0;
                        Debug.Log("7 " + cptToAbort);
                    }
                    else
                    {
                        Debug.Log("8 " + cptToAbort);
                        currentNumberTyped = 0;
                        text8.text = "MAUVAIS CODE, REESSAYEZ";
                        changeBack(text8);
                    }
                }
            }
        }
        else if (num == -3)
        {
            currentPage = PageSelec.E_HOME;
            currentNumberTyped = 0;
            isPageChanged = true;
            if (client != null)
            {
                server._server.EndTransaction(client);
                client = null;
            }
        
        }

        if (currentNumberTyped > 0)
        {
            InputText.text = "" + currentNumberTyped;
            if (currentPage == PageSelec.E_ENTER_CODE || currentPage == PageSelec.E_ENTER_UNIQUE_CODE_GG)
            {
                int nb = InputText.text.Length;
                InputText.text = "";
                for (int i = 0; i < nb; ++i)
                    InputText.text += '*';
            }
        }
        else
            InputText.text = "";

        if (isPageChanged == true)
            changeTo(currentPage);
    }


    public bool setCodeGG(int code)
    {
        bool isPageChanged = false;

        if (currentPage != PageSelec.E_ENTER_CODE_GG)
            return false;

        if (code == GoodCode)
        {
            currentPage = PageSelec.E_GOOD_CODE;
            currentNumberTyped = 0;
            isPageChanged = true;
            cptToAbort = 0;
            Debug.Log("1 " + cptToAbort);
        }
        else
        {
            ++cptToAbort;
            if (cptToAbort > 2)
            {
                currentPage = PageSelec.E_WRONG_CODE;
                currentNumberTyped = 0;
                isPageChanged = true;
                cptToAbort = 0;
                Debug.Log("2 " + cptToAbort);
            }
            else
            {
                Debug.Log("3 " + cptToAbort);
                currentNumberTyped = 0;
                text4.text = "MAUVAIS CODE, REESSAYEZ";
                changeBack(text8);
                server._server.SendPinStatus(client, false);
            }
        }

        if (isPageChanged == true)
            changeTo(currentPage);
        return true;
    }
}
