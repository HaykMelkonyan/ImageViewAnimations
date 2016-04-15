package com.example.user.imageviewanimations;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE1_PATH="/mnt/sdcard/DCIM/Camera/IMG_20160404_183018.jpg";
    private static final String IMAGE2_PATH="/mnt/sdcard/DCIM/Camera/IMG_20160404_183018.jpg";
    private static final String IMAGE3_PATH="/mnt/sdcard/DCIM/Camera/IMG_20160404_183018.jpg";
    private static final String IMAGE4_PATH="/mnt/sdcard/DCIM/Camera/IMG_20160404_183018.jpg";
    private static final int SELECT_IMAGE = 100;
    private static final int REQUEST_FOR_SHARE=111;
    private static  int current_index;


    int center_index=-1;
    private int imageViewHeight;
    ArrayList<ImageView> imageViewList = new ArrayList<ImageView>() ;
    ArrayList<String> pathList= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        current_index=0;

        ImageView img1= (ImageView)findViewById(R.id.image1);
        ImageView img2= (ImageView)findViewById(R.id.image2);
        ImageView img3= (ImageView)findViewById(R.id.image3);
        ImageView img4= (ImageView)findViewById(R.id.image4);
        imageViewList.add(img1 );
        imageViewList.add(img2);
        imageViewList.add(img3);
        imageViewList.add(img4);
        pathList.add(IMAGE1_PATH);
        pathList.add(IMAGE2_PATH);
        pathList.add(IMAGE3_PATH);
        pathList.add(IMAGE4_PATH);
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        imageViewHeight=(int)(80 * getApplicationContext().getResources().getDisplayMetrics().density);
        final int center_x = size.x/2-imageViewHeight/2;
        final int center_y = size.y/2-imageViewHeight/2;

        ImageView.OnClickListener listener= new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (center_index!=-1)
                {
                    ImageView currImage= imageViewList.get(center_index);
                    ObjectAnimator animteXCenter= ObjectAnimator.ofFloat(currImage,"X",center_index*imageViewHeight+ center_index*7);
                    ObjectAnimator animeYCenter=ObjectAnimator.ofFloat(currImage, "Y", 5);
                    ObjectAnimator scaleX= ObjectAnimator.ofFloat(currImage,"scaleX",1.0f);
                    scaleX.setRepeatCount(0);
                    ObjectAnimator scaleY= ObjectAnimator.ofFloat(currImage,"scaleY",1.0f);
                    scaleY.setRepeatCount(0);
                    AnimatorSet settoCenter= new AnimatorSet();
                    settoCenter.playTogether(animeYCenter, animteXCenter);
                    AnimatorSet scaleXY= new AnimatorSet();
                    scaleXY.playTogether(scaleX,scaleY);
                    AnimatorSet sss= new AnimatorSet();
                    sss.play(scaleXY).before(settoCenter);
                    sss.start();
                }

               ImageView currImage= (ImageView)v;
               ObjectAnimator animteXCenter= ObjectAnimator.ofFloat(currImage,"X",center_x);
               ObjectAnimator animeYCenter=ObjectAnimator.ofFloat(currImage, "Y", center_y);
               ObjectAnimator scaleX= ObjectAnimator.ofFloat(currImage,"scaleX",2.5f);
               ObjectAnimator scaleY= ObjectAnimator.ofFloat(currImage,"scaleY",2.5f);
               AnimatorSet settoCenter= new AnimatorSet();
               settoCenter.playTogether(animeYCenter, animteXCenter);
               AnimatorSet scaleXY= new AnimatorSet();
               scaleXY.playTogether(scaleX,scaleY);
               AnimatorSet sss= new AnimatorSet();
               sss.play(settoCenter).before(scaleXY);
               sss.start();
                center_index= imageViewList.indexOf(currImage);


            }
            };

        // img1.getHeight();
        for (ImageView item : imageViewList)
        {
            item.setOnClickListener(listener);
        }



        AsyncTask<Void,Bitmap,Void> task = new AsyncTask<Void,  Bitmap,Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected Void doInBackground(Void... params) {

                BitmapFactory.Options sizeOptions= new BitmapFactory.Options();
                sizeOptions.inJustDecodeBounds=true;

                for (String item : pathList)
                {
                    BitmapFactory.decodeFile(item, sizeOptions);
                    BitmapFactory.Options loadOptions= new BitmapFactory.Options();
                   loadOptions.inSampleSize= sizeOptions.outHeight/imageViewHeight;
                    Bitmap bmp= BitmapFactory.decodeFile(item,loadOptions);
                    publishProgress(bmp);


                }
                current_index=0;
                return  null;
            }

            @Override
            protected void onProgressUpdate(Bitmap... values) {
                super.onProgressUpdate(values);

                if(current_index<imageViewList.size())
                    imageViewList.get(current_index++).setImageBitmap(values[0]);
            }
        };
        task.execute();
        }








}
