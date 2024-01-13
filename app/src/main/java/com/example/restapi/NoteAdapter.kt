package com.example.restapi

import android.icu.text.CaseMap.Title
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restapi.databinding.NotesitemBinding
import java.lang.invoke.TypeDescriptor

class NoteAdapter(private val notes:List<NoteItem>, private val itemclickListener: AllNotesActivity):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnItemClickListener{
        fun onDeleteClick(noteId:String)
        fun onUpdateClick(noteId: String,title: String,description:String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding:NotesitemBinding = NotesitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
       val note:NoteItem = notes[position]
        holder.bind(note)
        holder.binding.deleteBtn.setOnClickListener{
            itemclickListener.onDeleteClick(note.noteId)
        }

        holder.binding.updateBtn.setOnClickListener{
            itemclickListener.onUpdateClick(note.noteId,note.title,note.description)
        }
    }

    class NoteViewHolder(val binding:NotesitemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(note:NoteItem){
            binding.tittleText.text = note.title
            binding.descriptionT.text = note.description
        }


    }
}