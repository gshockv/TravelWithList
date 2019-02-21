package com.gshockv.travelwishlist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.transition.Transition
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
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

    private lateinit var inputManager: InputMethodManager
    private lateinit var place: Place
    private lateinit var todoList: ArrayList<String>
    private lateinit var toDoAdapter: ArrayAdapter<*>

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
        if (!isEditTextVisible) {
            revealEditText(revealView)
            todoText.requestFocus()
            inputManager.showSoftInput(todoText, InputMethodManager.SHOW_IMPLICIT)

            addButton.setImageResource(R.drawable.icon_morph)
            val animatable = addButton.drawable as Animatable
            animatable.start()
        } else {
            val text = todoText.text.toString()
            if (!TextUtils.isEmpty(text)) {
                addToDo(text)
                toDoAdapter.notifyDataSetChanged()
            }
            inputManager.hideSoftInputFromWindow(todoText.windowToken, 0)
            hideEditText(revealView)

            addButton.setImageResource(R.drawable.icon_morph_reverse)
            val animatable = addButton.drawable as Animatable
            animatable.start()
        }
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
        window.enterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                addButton.animate().alpha(1.0f)
                window.enterTransition.removeListener(this)
            }

            override fun onTransitionResume(transition: Transition) { }
            override fun onTransitionPause(transition: Transition) { }
            override fun onTransitionCancel(transition: Transition) { }
            override fun onTransitionStart(transition: Transition) { }
        })
    }

    private fun addToDo(todo: String) {
        todoList.add(todo)
    }

    private fun getPhoto() {
        val photo = BitmapFactory.decodeResource(resources,
            place.imageResourceId(this))
        colorize(photo)
    }

    private fun colorize(photo: Bitmap) {
        val palette = Palette.from(photo).generate()
        applyPalette(palette)
    }

    private fun applyPalette(palette: Palette) {
        window.setBackgroundDrawable(ColorDrawable(palette.getDarkMutedColor(defaultColor)))
        placeNameHolder.setBackgroundColor(palette.getMutedColor(defaultColor))
        revealView.setBackgroundColor(palette.getLightVibrantColor(defaultColor))
    }

    private fun revealEditText(view: LinearLayout) {
        val cx = view.right - 30
        val cy = view.bottom - 60
        val finalRadius = Math.max(view.width, view.height)

        view.visibility = View.VISIBLE
        isEditTextVisible = true

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
        anim.start()
    }

    private fun hideEditText(view: LinearLayout) {
        val cx = view.right - 30
        val cy = view.bottom - 60
        val initialRadius = view.width

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.INVISIBLE
            }
        })
        isEditTextVisible = false
        anim.start()
    }

    override fun onBackPressed() {
        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.duration = 100
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
