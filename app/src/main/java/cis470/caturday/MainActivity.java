package cis470.caturday;

// Homework 3
// Rudolph Libertini
// 4/18/2017

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class MainActivity extends AppCompatActivity {

    ImageView Cat1;
    ImageView Cat2;
    ImageView Cat3;
    ImageView Cat4;
    Button button;
    GridLayout pictures;
    boolean oneOrFour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cat1 = (ImageView) findViewById(R.id.cat_pic1);
        Cat2 = (ImageView) findViewById(R.id.cat_pic2);
        Cat3 = (ImageView) findViewById(R.id.cat_pic3);
        Cat4 = (ImageView) findViewById(R.id.cat_pic4);
        pictures = (GridLayout) findViewById(R.id.pictures);
        button = (Button) findViewById(R.id.btn);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.one_cat_btn:
                if (checked)
                    // Show one cat image
                    oneOrFour = true;
                    Cat1.setVisibility(View.VISIBLE);
                    Cat2.setVisibility(View.GONE);
                    Cat3.setVisibility(View.GONE);
                    Cat4.setVisibility(View.GONE);
                    break;
            case R.id.four_cats_btn:
                if (checked)
                    // show four cat images
                    oneOrFour = false;
                    Cat1.setVisibility(View.VISIBLE);
                    Cat2.setVisibility(View.VISIBLE);
                    Cat3.setVisibility(View.VISIBLE);
                    Cat4.setVisibility(View.VISIBLE);
                    break;
        }
    }

    public void showCats(View view){
        String fourCatURL = "http://thecatapi.com/api/images/get?format=xml&results_per_page=4&size=small";
        String oneCatURL = "http://thecatapi.com/api/images/get?size=med";

        if(!oneOrFour){
            Log.d("URLS","Selected the four cats radio btn and clicked showCats btn");
            Ion.with(MainActivity.this)
                    .load(fourCatURL)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String xmlString) {
                            String[] fourCats = {"","","",""};
                            Log.d("URLS","Ion.setCallback() has been executed");
                            XmlToJson xmlToJson = new XmlToJson.Builder(xmlString).build();
                            JSONObject jsonObject = xmlToJson.toJson();
                            try {
                                JSONObject response = jsonObject.getJSONObject("response");
                                JSONObject data = response.getJSONObject("data");
                                JSONObject images = data.getJSONObject("images");
                                JSONArray image = images.getJSONArray("image");
                                Log.d("URLS", "JsonArray images should be populated at this point");
                                for(int i = 0; i < image.length(); i++){
                                    JSONObject imageInfo = image.getJSONObject(i);
                                    String url = imageInfo.getString("url");
                                    fourCats[i]= url;
                                    Log.d("URLS",url);
                                }
                                Picasso.with(MainActivity.this).load(fourCats[0]).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.error).placeholder(R.drawable.loading).fit().into(Cat1);
                                Picasso.with(MainActivity.this).load(fourCats[1]).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.error).placeholder(R.drawable.loading).fit().into(Cat2);
                                Picasso.with(MainActivity.this).load(fourCats[2]).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.error).placeholder(R.drawable.loading).fit().into(Cat3);
                                Picasso.with(MainActivity.this).load(fourCats[3]).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.error).placeholder(R.drawable.loading).fit().into(Cat4);


                            } catch (JSONException e1) {
                                Log.d("URLS", e1.getMessage());
                                e1.printStackTrace();
                            }
                        }
                    });


        }else
            Picasso.with(MainActivity.this).load(oneCatURL).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.error).placeholder(R.drawable.loading).fit().into(Cat1);
    }

}

