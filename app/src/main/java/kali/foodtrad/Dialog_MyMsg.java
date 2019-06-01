package kali.foodtrad;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by elliotching on 07-Mar-17.
 */

class Dialog_MyMsg{
    Dialog d;
    TextView text;
    Dialog_MyMsg(Context context, String msg){
        d = new Dialog(context);
        d.setContentView(R.layout.dialog_simple_msg_layout);
        text = (TextView) d.findViewById(R.id.dialog_msg_textview);
        text.setText(msg);
//        d.set
        d.setTitle("Notice");
        d.show();
    }
}
