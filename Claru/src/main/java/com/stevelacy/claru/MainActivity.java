package com.stevelacy.claru;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {


    // This is Your server URL with the port if needed (ie: http://localhost:5000)
    String url = "http://node.local";
    WebView webView;



    private class MyWebViewClient extends WebViewClient {

        // If unable to reach the server (internet issues?)
        @Override
        public void onReceivedError(WebView view, int errorCode, String
                description, String failingUrl) {
            view.loadUrl("file:///android_asset/noservice.html");
        }


    }

    // Handle the back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Clárú");

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("claru-app");
        webView.setWebViewClient(new MyWebViewClient() );
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new:
                newNote();
                break;
            case R.id.action_list:
                showList();
                break;
            case R.id.action_user:
                showUser();
        }
        return super.onOptionsItemSelected(item);
    }


// Functions


    public void newNote(){
        // Create a dialog to show the input for a new note
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater =  this.getLayoutInflater();

        // Get the view from layout/dialog_new
        final View dialogNew = inflater.inflate(R.layout.dialog_new, null);
        // Build the dialog
        builder.setView(dialogNew).setTitle(R.string.action_new)
                .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText editTextNew = (EditText) dialogNew.findViewById(R.id.newNote);
                        String title = editTextNew.getText().toString();

                        // Send the title to postNew()....
                        postNew(title);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void postNew(String title){
        // Post the title to the Claru server
        String postdata = "newNote=" + title;
        webView.postUrl(url, postdata.getBytes());
        // Send feedback for fun
        CharSequence test = "Creating";
        Toast.makeText(this, test, Toast.LENGTH_LONG).show();

    }

    // Show the list
    public void showList(){
        webView.loadUrl(url);
    }
    public void showUser(){
        // TODO -> make this!
    }

}
