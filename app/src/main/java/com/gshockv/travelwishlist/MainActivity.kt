package com.gshockv.travelwishlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.core.util.Pair
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*

class MainActivity : AppCompatActivity() {
    private lateinit var menu: Menu
    private var isListView: Boolean = true

    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var adapter: TravelListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        staggeredGridLayoutManager = StaggeredGridLayoutManager(1,
            StaggeredGridLayoutManager.VERTICAL)
        recyclerList.layoutManager = staggeredGridLayoutManager
        adapter = TravelListAdapter(this)
        recyclerList.adapter = adapter
        adapter.setOnItemClickListener(onItemClickListener)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.appToolbar))
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowTitleEnabled(true)
            it.elevation = 8.0f
        }
    }

    private val onItemClickListener = object : TravelListAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            openPlaceDetails(view, position)
        }
    }

    private fun openPlaceDetails(view: View, position: Int) {
        val transitionIntent = DetailsActivity.newIntent(this@MainActivity, position)
        val placeImage = view.findViewById<ImageView>(R.id.placeImage)
        val placeNameHolder = view.findViewById<LinearLayout>(R.id.placeNameHolder)

        val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
        val statusBar = findViewById<View>(android.R.id.statusBarBackground)

        val imagePair = Pair.create(placeImage as View, "tImage")
        val holderPair = Pair.create(placeNameHolder as View, "tNameHolder")

        val navPair = Pair.create(navigationBar,
            Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
        val statusPair = Pair.create(statusBar,
            Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
        val toolbarPair = Pair.create(appToolbar as View, "tActionBar")

        val pairs = mutableListOf(
            imagePair,
            holderPair,
            statusPair,
            toolbarPair
        )
        if (navigationBar != null && navPair != null) {
            pairs += navPair
        }

        val options = ActivityOptionsCompat
            .makeSceneTransitionAnimation(this@MainActivity, *pairs.toTypedArray())
        ActivityCompat.startActivity(this@MainActivity, transitionIntent, options.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_toggle) {
            toggle()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggle() {
        if (isListView) {
            showGridView()
        } else {
            showListView()
        }
    }

    private fun showListView() {
        staggeredGridLayoutManager.spanCount = 1
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_grid)
        item.title = getString(R.string.show_as_grid)
        isListView = true
    }

    private fun showGridView() {
        staggeredGridLayoutManager.spanCount = 2
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_list)
        item.title = getString(R.string.show_as_list)
        isListView = false
    }
}
