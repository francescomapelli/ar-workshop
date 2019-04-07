package com.nytimes.android.ramonaharrison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.AugmentedFace
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.AugmentedFaceNode
import kotlinx.android.synthetic.main.content_faces.*


class FacesActivity : AppCompatActivity() {


     var faceRegionsRenderable : ModelRenderable? = null
     var faceMeshTexture : Texture? = null

    private lateinit var arFragment: ArFragment
    val faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faces)
        arFragment = fragment as ArFragment
        val sceneView = arFragment.arSceneView
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST


        val scene = sceneView.scene

        scene.addOnUpdateListener { frameTime: FrameTime ->
            if (faceRegionsRenderable != null && faceMeshTexture != null) {
                handleTrackedFaces(sceneView, scene)
                handleUntrackedFaces()
            }
        }

        loadFaceRenderable()
        loadFaceTexture()



    }

    private fun handleTrackedFaces(sceneView: ArSceneView, scene: Scene) {
        val faceList = sceneView.session?.getAllTrackables(AugmentedFace::class.java) ?: emptyList()
        for (face in faceList) {
            if (!faceNodeMap.containsKey(face)) {
                val faceNode = AugmentedFaceNode(face)
                faceNode.setParent(scene)
                faceNode.faceRegionsRenderable = faceRegionsRenderable
                faceNode.faceMeshTexture = faceMeshTexture
                faceNodeMap[face] = faceNode
            }
        }
    }


    private fun handleUntrackedFaces() {
        val iterator = faceNodeMap.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val face = entry.key
            if (face.trackingState == TrackingState.STOPPED) {
                val faceNode = entry.value
                faceNode.setParent(null)
                iterator.remove()
            }
        }
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
