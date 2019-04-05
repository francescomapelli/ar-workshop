package com.nytimes.android.ramonaharrison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.content_faces.*


class FacesActivity : AppCompatActivity() {


    lateinit var faceRegionsRenderable : ModelRenderable
    lateinit var faceMeshTexture : Texture

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faces)
        arFragment = fragment as ArFragment
        val sceneView = arFragment.arSceneView
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST
        loadFaceRenderable()
        loadFaceTexture()
    }


    private fun loadFaceRenderable() {
        ModelRenderable.builder()
            .setSource(this, R.raw.fox_face)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                modelRenderable.isShadowCaster = false
                modelRenderable.isShadowReceiver = false
                faceRegionsRenderable = modelRenderable
            }
    }

    private fun loadFaceTexture() {
        Texture.builder()
            .setSource(this, R.drawable.fox_face_mesh_texture)
            .build()
            .thenAccept({ texture: Texture ->
                faceMeshTexture = texture
            })
    }


}
