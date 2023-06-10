package com.example.seatreservationemployee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.models.Issue

class IssuesAdapter (private val clickListener: (Issue) -> Unit) : RecyclerView.Adapter<IssuesAdapter.IssuesViewHolder>(){

    private var issuesList: List<Issue> = listOf()

    class IssuesViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IssuesViewHolder {
        return IssuesViewHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.issue_element,
            parent,
            false)) {
            clickListener(issuesList[it])
        }
    }

    override fun getItemCount() = issuesList.size

    override fun onBindViewHolder(holder: IssuesViewHolder, i: Int) {
        holder.itemView.apply {
            this.findViewById<TextView>(R.id.ticket_email).text = issuesList[i].email
            this.findViewById<TextView>(R.id.ticket_title).text = issuesList[i].title
        }
    }

    fun updateList(newIssueList: List<Issue>){
        issuesList = newIssueList
        notifyDataSetChanged()
    }

}

