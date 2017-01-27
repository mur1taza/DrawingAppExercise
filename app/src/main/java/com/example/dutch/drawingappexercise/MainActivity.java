package com.example.dutch.drawingappexercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {private DrawingView mDrawView;
    private ImageButton mIvCurrPaint;
    private float mxSmallBrush, mSmallBrush, mMediumBrush, mLargeBrush,mxLargeBrush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get brush sizes from resources
        mxSmallBrush = getResources().getInteger(R.integer.extra_small);
        mSmallBrush = getResources().getInteger(R.integer.small_size);
        mMediumBrush = getResources().getInteger(R.integer.medium_size);
        mLargeBrush = getResources().getInteger(R.integer.large_size);
        mxLargeBrush = getResources().getInteger(R.integer.extra_large_size);
        //get drawing view's instance
        mDrawView = (DrawingView) findViewById(R.id.drawing);
        //set current color view as selected
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        if (paintLayout != null) {
            mIvCurrPaint = (ImageButton) paintLayout.getChildAt(0);
            if (mIvCurrPaint != null) {
                mIvCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            }
        }
        //set click listeners on new drawing button
        ImageButton btnNew = (ImageButton) findViewById(R.id.btn_new);
        if (btnNew != null) {
            btnNew.setOnClickListener(this);
        }
        //set click listeners on brush button
        ImageButton btnDraw = (ImageButton) findViewById(R.id.btn_draw);
        if (btnDraw != null) {
            btnDraw.setOnClickListener(this);
        }
        //set click listeners on erase button
        ImageButton btnErase = (ImageButton) findViewById(R.id.btn_erase);
        if (btnErase != null) {
            btnErase.setOnClickListener(this);
        }
        //set click listeners on save button
        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(this);
        }
        ImageButton btnOpacity = (ImageButton) findViewById(R.id.opacity_btn);
        if (btnOpacity != null) {
            btnOpacity.setOnClickListener(this);
        }
        //set initial brush size
        mDrawView.setBrushSize(mMediumBrush);
    }
    //use chosen color
    public void paintClicked(View view) {
        //set erase as false (if set previously as true)
        mDrawView.setErase(false);
        //reset brush size (if some other option was selected previously)
        mDrawView.setBrushSize(mDrawView.getLastBrushSize());
        //update color
        if (view != mIvCurrPaint) {
            //get the clicked image button
            ImageButton ivColorPallet = (ImageButton) view;
            //get color from tag
            String color = view.getTag().toString();
            //set new brush color
            mDrawView.setColor(color);
            //set current button's background as pressed button
            ivColorPallet.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            //set previous button's background as unpressed button
            mIvCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            //set current view as this button view
            mIvCurrPaint = (ImageButton) view;
        }
    }
    //use chosen patten
    public void patternClicked(View view) {
        //set erase as false (if set previously as true)
        mDrawView.setErase(false);
        //reset brush size (if some other option was selected previously)
        mDrawView.setBrushSize(mDrawView.getLastBrushSize());
        //update pattern
        if (view != mIvCurrPaint) {
            //get the clicked image button
            ImageButton ivPatten = (ImageButton) view;
            //set patten to brush
            ivPatten.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            //set previous button's background as unpressed button
            mIvCurrPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            //set current view as this button view
            mIvCurrPaint = (ImageButton) view;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new:
                //new button
                Toast.makeText(MainActivity.this, "New Drawing", Toast.LENGTH_SHORT).show();
                setNewCanvas();
                break;
            case R.id.btn_draw:
                //draw button clicked
                Toast.makeText(MainActivity.this, "Brush Size", Toast.LENGTH_SHORT).show();
                setBrushSize();
                break;
            case R.id.btn_erase:
                //switch to erase - choose size
                Toast.makeText(MainActivity.this, "Erase", Toast.LENGTH_SHORT).show();
                switchToEraseMode();
                break;
            case R.id.btn_save:
                //save drawing
                Toast.makeText(MainActivity.this, "Save", Toast.LENGTH_SHORT).show();
                saveDrawing();
                break;
            case R.id.opacity_btn:
                //change opacity
                Toast.makeText(MainActivity.this, "Opacity", Toast.LENGTH_SHORT).show();
                opacityChoser();
                break;

            default:
                break;
        }
    }
    private void setNewCanvas() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Are you sure you want to start a new drawing? (you will lose the current drawing)");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDrawView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }
    private void setBrushSize() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.brush_chooser);
        ImageButton xsmallBtn = (ImageButton) brushDialog.findViewById(R.id.extra_small_brush);
        xsmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mxSmallBrush);
                mDrawView.setLastBrushSize(mxSmallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mSmallBrush);
                mDrawView.setLastBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mMediumBrush);
                mDrawView.setLastBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mLargeBrush);
                mDrawView.setLastBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton xlargeBtn = (ImageButton) brushDialog.findViewById(R.id.extra_large_brush);
        xlargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(false);
                mDrawView.setBrushSize(mxLargeBrush);
                mDrawView.setLastBrushSize(mxLargeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }
    private void switchToEraseMode() {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Eraser size:");
        brushDialog.setContentView(R.layout.brush_chooser);
        ImageButton xsmallBtn = (ImageButton) brushDialog.findViewById(R.id.extra_small_brush);
        xsmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mxSmallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton xlargeBtn = (ImageButton) brushDialog.findViewById(R.id.extra_large_brush);
        xlargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setErase(true);
                mDrawView.setBrushSize(mxLargeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }
    //save drawing to gallery
    private void saveDrawing() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //save drawing
                mDrawView.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), mDrawView.getDrawingCache(),
                        UUID.randomUUID().toString() + ".png", "drawing");
                if (imgSaved != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Sorry.. Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                mDrawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }
    //Change opacity
    private void opacityChoser(){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View Viewlayout = inflater.inflate(R.layout.opacity_chooser,
                (ViewGroup) findViewById(R.id.layout_opacity));
        final TextView seekTxt = (TextView)Viewlayout.findViewById(R.id.opq_txt);
        final SeekBar seekOpq = (SeekBar)Viewlayout.findViewById(R.id.opacity_seek);
        popDialog.setTitle("Opacity level:");
        popDialog.setView(Viewlayout);
        //set max
        seekOpq.setMax(100);
        //show current level
        int currLevel = mDrawView.getPaintAlpha();
        seekTxt.setText(currLevel+"%");
        seekOpq.setProgress(currLevel);
        //update as user interacts
        seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Do something here with new value
                seekTxt.setText(Integer.toString(progress) + "%");
            }
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            });
            // Button OK
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDrawView.setPaintAlpha(seekOpq.getProgress());
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
        }
    }

