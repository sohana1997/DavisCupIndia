package com.os.daviscupindia.ui.home.ui.home

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.os.daviscupindia.WebViewActivity
import com.os.daviscupindia.databinding.FragmentHomeBinding
import com.os.daviscupindia.ui.home.MainViewModel
import com.os.daviscupindia.ui.home.adapter.NewsAdapter
import com.os.daviscupindia.ui.home.adapter.RvAdapter
import com.os.daviscupindia.ui.home.adapter.RvVideoAdapter
import com.os.daviscupindia.ui.home.adapter.SponsoredAdapter
import com.os.daviscupindia.ui.home.model.ItemsViewModel
import com.os.daviscupindia.ui.home.model.New
import com.os.daviscupindia.ui.home.model.Sponsor
import com.os.daviscupindia.ui.home.model.Video
import com.os.daviscupindia.utils.MyApplication
import com.os.daviscupindia.utils.ProgressDialog
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val list = mutableListOf<CarouselItem>()
    private var playerlist = ArrayList<ItemsViewModel>()
    private var newslist = ArrayList<New>()
    var sponsoredList = ArrayList<Sponsor>()
    var videoList = ArrayList<Video>()
    var adapter: SponsoredAdapter? = null
    var runnable: Runnable? = null

    //handle scroll count
    var scrollCount: Int = 0

    private lateinit var layoutManager: LinearLayoutManager

    //handler for run auto scroll thread
    internal val handler = Handler()
    val displayMetrics = DisplayMetrics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ProgressDialog.showProgressDialog(requireActivity())

        homeViewModel.homeDataModel.observe(viewLifecycleOwner, Observer { it ->
            it.data.banner.map {
                list.add(CarouselItem(imageUrl = it.image_url, caption = it.title))
            }
            it.data.player.map {
                playerlist.add(ItemsViewModel(image = it.image, text = it.title, id = it._id))
            }

            newslist = it.data.news
            videoList = it.data.video
            sponsoredList = it.data.sponsor
            initLayoutManager()
            setVideos()
            setNews()
            setBanner()
            setPlayer()
            ProgressDialog.hideProgressDialog()
            MyApplication.tinyDB.putString("privacyPolicy", it.data.privacyPolicy)
            MyApplication.tinyDB.putString("tramAndConditions", it.data.tramAndConditions)

        })

        homeViewModel.settingData.observe(viewLifecycleOwner, Observer { dataModel ->
            if (dataModel.data.is_live == "Yes") {
                binding.liveView.rootLive.isVisible = true
                // setup Glide request without the into() method

                Glide.with(requireActivity())
                    .asBitmap()
                    .load("http://img.youtube.com/vi/kOZO3agWD0c/hqdefault.jpg")
                    .into(binding.liveView.liveViewApp.appImg)

                binding.liveView.liveViewApp.play.setOnClickListener {
                    requireActivity().startActivity(
                        Intent(requireActivity(), WebViewActivity::class.java).putExtra(
                            "url",
                            dataModel.data.streaming
                        ).putExtra(
                            "title",
                            ""
                        )
                    )

                }


            }

            MyApplication.tinyDB.putString("support_email", dataModel.data.support_email)
            MyApplication.tinyDB.putString("number", dataModel.data.email)

        })
        return root
    }

    private fun setBanner() {
        binding.carousel.apply {
            registerLifecycle(lifecycle)
            autoPlay = true
            setData(list)
        }

    }

    private fun setPlayer() {

        binding.playerView.playerList.adapter =
            activity?.let { RvAdapter(itemsViewModel = playerlist, mcontext = it) }
    }

    private fun setNews() {
        binding.news.newsList.adapter =
            activity?.let { NewsAdapter(itemsViewModel = newslist, mcontext = it) }
    }

    private fun setVideos() {
        binding.videos.videoList.adapter =
            activity?.let { RvVideoAdapter(itemsViewModel = videoList, mcontext = it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        runnable?.let { handler.removeCallbacks(it) }
        _binding = null
    }

    private fun initLayoutManager() {

        layoutManager = object : LinearLayoutManager(activity) {
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView,
                state: RecyclerView.State?,
                position: Int
            ) {
                val smoothScroller = object : LinearSmoothScroller(activity) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                        return 5.0f
                    }
                }
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }
        adapter = object : SponsoredAdapter(sponsoredList, requireActivity()) {
            override fun load() {
                if (layoutManager.findFirstVisibleItemPosition() > 1) {
                    adapter?.notifyItemMoved(0, sponsoredList.size - 1)
                }
            }
        }

        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.sponsored.sponsoredList.layoutManager = layoutManager
        binding.sponsored.sponsoredList.setHasFixedSize(true)
        binding.sponsored.sponsoredList.setItemViewCacheSize(10)
        binding.sponsored.sponsoredList.adapter = adapter
        autoScroll()
    }


    private fun autoScroll() {
        if (binding != null) {
            scrollCount = 0
            val speedScroll: Long = 1200
            runnable = object : Runnable {
                override fun run() {
                    if (layoutManager.findFirstVisibleItemPosition() >= sponsoredList.size / 2) {
                        adapter?.load()
                        Log.e("TAG", "run: load $scrollCount")
                    }
                    binding.sponsored.sponsoredList.smoothScrollToPosition(scrollCount++)
                    Log.e("TAG", "run: $scrollCount")
                    handler.postDelayed(this, speedScroll)
                }
            }
            handler.postDelayed(runnable as Runnable, speedScroll)
        }
    }

    @Throws(Throwable::class)
    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap())
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }
}