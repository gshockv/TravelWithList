package com.gshockv.travelwishlist

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gshockv.travelwishlist.data.Place
import com.gshockv.travelwishlist.data.PlaceData
import com.gshockv.travelwishlist.data.imageResourceId
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PARAM_ID = "position_param_extra_id"

        fun newIntent(context: Context, position: Int)
                = Intent(context, DetailsActivity::class.java).apply {
            putExtra(EXTRA_PARAM_ID, position)
        }
    }

    lateinit private var inputManager: InputMethodManager
    lateinit private var place: Place
    lateinit private var todoList: ArrayList<String>
    lateinit private var toDoAdapter: ArrayAdapter<*>

    private var isEditTextVisible: Boolean = false
    private var defaultColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupValues()
        setUpAdapter()
        loadPlace()
        windowTransition()
        getPhoto()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupValues() {
        place = PlaceData.placeList()[intent.getIntExtra(EXTRA_PARAM_ID, 0)]
        addButton.setOnClickListener { addButtonClick() }
        defaultColor = ContextCompat.getColor(this, R.color.primary_dark)
        inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        revealView.visibility = View.INVISIBLE
        isEditTextVisible = false
    }

    private fun addButtonClick() {

    }

    private fun setUpAdapter() {
        todoList = ArrayList()
        toDoAdapter = ArrayAdapter(this, R.layout.row_todo, todoList)
        activitiesList.adapter = toDoAdapter
    }

    private fun loadPlace() {
        placeTitle.text = place.name
        placeImage.setImageResource(place.imageResourceId(this))
    }

    private fun windowTransition() {

    }

    private fun addToDo(todo: String) {
        todoList.add(todo)
    }

    private fun getPhoto() {
        val photo = BitmapFactory.decodeResource(resources, place.imageResourceId(this))
    }

    private fun colorize(photo: Bitmap) {}

    private fun applyPalette() {

    }

    private fun revealEditText(view: LinearLayout) {

    }

    private fun hideEditText(view: LinearLayout) {

    }

    override fun onBackPressed() {
        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.duration = 1000
        addButton.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                addButton.visibility = View.GONE
                finishAfterTransition()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }
}
