package kali.foodtrad;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by elliotching on 26-Nov-16.
 */
interface InterfaceAsyncRequestData {

    /*
    * This is Interface used when a VALUE obtained from
    * an Async Thread (Class) has to be pass over to another
    * Class (probably UI thread for display to user),
    * but Async Thread and UI Thread are seperated in 2 different class.
    *
    * NOTE:
    * Implementation for these methods are
    * preferable to be
    * inside the necessary displayed UI Activity, e.g. MainActivity
    *
    * Summary:
    * - UI Thread               = setup the methods, (activityRequestData = this), just for standby, NOT TO BE CALLED!
    *
    * - Background/Async Thread = invoke/call the methods, activityRequestData=null
    *
    * - Interface (or I called it "Skin") here = just a skin / head / methods name only, without implementation
    *
    *
    * UI Thread:
    * -implements and link->>
    *
    * ClassName implements AsyncRespondInterface {
    *     ClassName(){
    *         Async.activityRequestData = this;
    *     }
    *     // implements the rest of the methods()
    * }
    *
    * Background/Async Thread:
    * -prepare->>   AsyncRespondInterface activityRequestData = null;
    * -invoke->>   AsyncRespondInterface.someMethods();
    *
    */

    void getArrayListJSONResult(ArrayList<JSONObject> responses);
}
