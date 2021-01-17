//package com.ahmedg.tripplannerpro.view;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import com.ahmedg.tripplannerpro.R;
//
//public class CustomDialoge extends DialogFragment {
//    Button buttonStart;
//    Button buttonCancel;
//    TextView textView;
//
//
//    @NonNull
//  //  @Override
////    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
////
////        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
////
////
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////
////
////        builder.setTitle("TRIP");
////
////
////        builder.setMessage("YOUR TRIP IS HERE");
////
////
////
////        return builder.create();
////    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialoge1, container, false);
////        buttonStart = view.findViewById(R.id.buttonStart);
////        buttonCancel = view.findViewById(R.id.buttonCancel);
////        textView = view.findViewById(R.id.textViewDialog);
//        textView.setText("YOUR TRIP IS HERE");
//        buttonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//            }
//        });
//        buttonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri location = Uri.parse("google.navigation:q=Cairo+Egypt");
//                Intent intent2 = new Intent(Intent.ACTION_VIEW, location);
//                intent2.setPackage("com.google.android.apps.maps");
//                startActivity(intent2);
//            }
//        });
//
//
//        return view;
//    }
//}
