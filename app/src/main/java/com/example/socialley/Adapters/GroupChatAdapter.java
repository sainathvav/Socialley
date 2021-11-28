package com.example.socialley.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialley.Models.MessageModel;
import com.example.socialley.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;


    int SENDER_VIEW_TYPE =1;
    int RECEIVER_VIEW_TYPE =2;

    public GroupChatAdapter(ArrayList<MessageModel>messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new RecieverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModels.get(position);


        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
            Long timeStamp = messageModel.getTimeStamp();
            Date date = new Date(timeStamp);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            ((SenderViewHolder)holder).senderTime.setText(format.format(date));
//            ((RecieverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
        }else {
            ((RecieverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
//            Log.i("Receiver", messageModel.getUserName());
            ((RecieverViewHolder)holder).receiverName.setText(messageModel.getUserName());
            Long timeStamp = messageModel.getTimeStamp();
            Date date = new Date(timeStamp);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            ((RecieverViewHolder)holder).receiverTime.setText(format.format(date));
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg, receiverTime,receiverName;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            receiverName = itemView.findViewById(R.id.receiverName);


        }
    }

    public class  SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }
}
