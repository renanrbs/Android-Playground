package com.playground

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.draganddrop.DropHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.playground.databinding.ActivityDragAndDropBinding

class DragAndDropActivity : AppCompatActivity() {

    private val draggableTexts = arrayListOf(
        "Bolacha Maria",
        "Bolacha de Água e Sal",
        "Biscoito de Polvilho",
        "Waffer",
        "Salgadinho"
    )

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var carrouselAdapter: CarrouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDragAndDropBinding.inflate(layoutInflater)

        configureDraggableTextCarrousel(binding)
        configureTextHolderArea(binding)

        setContentView(binding.root)
    }

    private fun configureTextHolderArea(binding: ActivityDragAndDropBinding) {
        DropHelper.configureView(
            this,
            // Target drop view to be highlighted
            binding.textHolderArea,
            // Supported MIME types
            arrayOf(MIMETYPE_TEXT_PLAIN)
        ) { _, payload ->
            binding.draggedText.text = payload.clip.getItemAt(0).text

            payload
        }
    }

    private fun configureDraggableTextCarrousel(binding: ActivityDragAndDropBinding) {
        linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        carrouselAdapter = CarrouselAdapter(draggableTexts, applicationContext)

        binding.textCarrousel.apply {
            layoutManager = linearLayoutManager
            adapter = carrouselAdapter
        }
    }

    class CarrouselAdapter(private val items: ArrayList<String>, private val context: Context) : RecyclerView.Adapter<CarrouselAdapter.TextHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrouselAdapter.TextHolder {
            val inflatedView = LayoutInflater.from(context).inflate(
                R.layout.text_card,
                parent,
                false
            )

            return TextHolder(inflatedView)
        }

        override fun onBindViewHolder(holder: CarrouselAdapter.TextHolder, position: Int) {
            val currentText = items[position]
            holder.bindText(currentText)

            // DragStartHelper didn't worked
            holder.itemView.setOnLongClickListener { view ->
                // Data to be dragged
                val clipData = ClipData.newPlainText(
                    view.tag as? CharSequence,
                    currentText
                )

                clipData.addItem(ClipData.Item("Biscoito"))

                // Item that is dragged
                val dragShadow = View.DragShadowBuilder(view)

                view.startDragAndDrop(
                    clipData,         // The data to be dragged
                    dragShadow,       // The drag shadow builder
                    null, // No need to use local data
                    0           // Flags (not currently used, set to 0)
                )

                // Indicate that the long-click was handled.
                true
            }
        }

        override fun getItemCount(): Int = items.size

        class TextHolder(private var view: View): RecyclerView.ViewHolder(view) {
            fun bindText(text: String) {
                view.findViewById<TextView>(R.id.internalText).text = text
            }
        }
    }
}