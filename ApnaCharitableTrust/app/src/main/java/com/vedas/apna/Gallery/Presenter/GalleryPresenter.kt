package com.vedas.apna.Gallery.Presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vedas.apna.Documents.Adapter.DocumentsAdapter
import com.vedas.apna.Documents.Model.AduitReport
import com.vedas.apna.Documents.Model.DocumentsModel
import com.vedas.apna.Gallery.Adapter.GalleryAdapter
import com.vedas.apna.Gallery.Model.GalleryModel
import com.vedas.apna.Gallery.Model.ImagesModel
import com.vedas.apna.Gallery.View.IgalleryView
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.ServerConnections.AppStatus
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import org.json.JSONException
import org.json.JSONObject

class GalleryPresenter : IGalleryPresenter, RetrofitCallbacks.ServerResponseInterface {
    private lateinit var galleryView: IgalleryView
    lateinit var context: Context
    private lateinit var galleryarray: ArrayList<GalleryModel>
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: GalleryAdapter
    private lateinit var adapters: DocumentsAdapter

    fun GalleryPresenter(galleryVieww: IgalleryView, context: Context) {
        this.galleryView = galleryVieww
        this.context = context
        this.galleryarray = ArrayList()
    }

    override fun successCallBack(body: String?, from: String?) {
        var jsonObject: JSONObject? = null
        var gm: GalleryModel
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    if (from.equals("gallery")) {
                        val imagesModels: ArrayList<ImagesModel> = ArrayList()
                        var ars: ImagesModel
                        var cName =""
                        for (item in 0 until jsonObject.getJSONArray("result").length()) {
                            val imagesModel: ArrayList<ImagesModel> = ArrayList()
                            cName = jsonObject.getJSONArray("result").getJSONObject(item).getString("categoryName")
                            for (k in 0 until jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("images").length()) {
                                ars = ImagesModel(jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("images").getJSONObject(k).getString("imgName"),
                                        jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("images").getJSONObject(k).getString("imgPath"),
                                        jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("images").getJSONObject(k).getString("imgInsertedOn"))
                                imagesModel.add(ars)
                                imagesModels.add(ars)
                                Log.e("items", ":::" + imagesModels.size.toString() + ", " + imagesModel.size.toString())
                            }
                            Log.e("auditreps ", " "+imagesModel.size.toString())
                            gm = GalleryModel(cName,imagesModel)
                            galleryarray.add(gm)
                        }
                        imagesModels.sortWith { o1, o2 -> o2.imgInsertedOn.compareTo(o1.imgInsertedOn) }
                        Log.e("item1", ":::" + galleryarray.size.toString())
                        galleryView.galleryresult(context, galleryarray,imagesModels, recyclerView, adapter)
                        ProgressDialog.getInstance().hideProgress()
                    } else {
                        val documentsLists: ArrayList<DocumentsModel> = ArrayList()
                        var ars: AduitReport
                        var dModels: DocumentsModel
                        var cName =""
                        for (item in 0 until jsonObject.getJSONArray("result").length()) {
                            val auditreps: ArrayList<AduitReport> = ArrayList()
                            cName = jsonObject.getJSONArray("result").getJSONObject(item).getString("categoryName")
                            for (k in 0 until jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("documents").length()) {
                                ars = AduitReport(jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("documents").getJSONObject(k).getString("docName"),
                                        jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("documents").getJSONObject(k).getString("docPath"),
                                        jsonObject.getJSONArray("result").getJSONObject(item).getJSONArray("documents").getJSONObject(k).getString("docInsertedOn"))
                                auditreps.add(ars)
                            }
                            Log.e("auditreps ", " "+auditreps.size.toString())
                            dModels = DocumentsModel(cName,auditreps)
                            documentsLists.add(dModels)
                        }
                        Log.e("item1", ":::" + documentsLists.size.toString())
                        galleryView.documentresult(context, documentsLists, recyclerView, adapters)
                        ProgressDialog.getInstance().hideProgress()
                    }
                }else{
                    galleryView.ToastMsg(jsonObject.getString("message"))
                    ProgressDialog.getInstance().hideProgress()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String) {
        galleryView.ToastMsg(failureMsg)
        ProgressDialog.getInstance().hideProgress()
    }

    override fun callGallery(recyclerView: RecyclerView, adapter: GalleryAdapter,progress:Boolean) {
        this.recyclerView = recyclerView
        this.adapter = adapter
        if (AppStatus.getInstance(context).isConnected()) {
            if (progress){
                ProgressDialog.getInstance().showProgress(context)
            }
            RetrofitCallbacks.getInstace().ApiCallbacksGallery(context, "admin/getGalary", "gallery")
        } else {
            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun callDocument(recyclerView: RecyclerView, adapter: DocumentsAdapter) {
        this.recyclerView = recyclerView
        this.adapters = adapter
        if (AppStatus.getInstance(context).isConnected()) {
            ProgressDialog.getInstance().showProgress(context)
            RetrofitCallbacks.getInstace().ApiCallbacksGallery(context, "admin/getdocs", "documents")
        } else {
            Toast.makeText(context, "No Internet Connection!!!!", Toast.LENGTH_SHORT).show()
        }
    }
}
