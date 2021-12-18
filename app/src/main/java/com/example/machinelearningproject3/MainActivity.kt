package com.example.machinelearningproject3

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.imageView1).setImageBitmap(imageBitmap)

//Prepare images for ML Kit APIs, which creates an InputImage object from a bitmap Image Object
            val imageForMLK = InputImage.fromBitmap(imageBitmap, 0)

//Initialize ML Kit API used, which is Vision Api (Image Labeling); to label objects in an image, pass the InputImage object to the ImageLabeler's process method.
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

//Share image with ML Kit API
            labeler.process(imageForMLK)
                .addOnSuccessListener { labels ->
                    Log.e("Dee", "Successful image process")
                    for (label in labels) {
                        val text = label.text//Label text description
                        val confidence = label.confidence * 100 //Confidence of image object
                        Log.i("Dee", "detected: " + text + " with confidence: " + confidence)
                        findViewById<TextView>(R.id.textView).append("$text $confidence% \n")
                    }
                }
                .addOnFailureListener {
                    Log.i("Dee", "Failed image process")
                    //Get info about labeled objects within an image
                }
        }
    }
}
