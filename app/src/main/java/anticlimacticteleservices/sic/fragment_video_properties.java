package anticlimacticteleservices.sic;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.text.HtmlCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_video_properties.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_video_properties#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_video_properties extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PassedVideo = "video";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Video mVideo;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_video_properties() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_video_properties.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_video_properties newInstance(Video param1, String param2) {
        fragment_video_properties fragment = new fragment_video_properties();
        Bundle args = new Bundle();
        args.putSerializable(PassedVideo, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideo = (Video)getArguments().getSerializable(PassedVideo);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_properties, container, false);
        Video vid = mVideo;
        new VideoScrape().execute(vid);
        List<Comment> comments = new ArrayList<>();
        comments = MainActivity.masterData.getCommentDao().getCommentsByFeedId(vid.getID());
        TextView textView = v.findViewById(R.id.video_description);
        Spanned spanned = HtmlCompat.fromHtml(vid.getDescription(), HtmlCompat.FROM_HTML_MODE_COMPACT);
        String description=vid.getDescription()+"<p>";
        description = description + vid.toHtmlString();
        if (comments.size()>0){
            description=description+"<p><h2>Comments:</h2><p>";
        }
        for (Comment c : comments) {
            description = description + c.toHtml();
        }
        textView.setText(Html.fromHtml(description));
        ImageView image = v.findViewById(R.id.thumbNailView);
        Picasso.get().load(vid.getThumbnail()).into(image);
        TextView title = v.findViewById(R.id.video_name);
        title.setText(vid.getTitle());
        Button dialogButton = v.findViewById(R.id.closeButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.masterData.fragmentManager.popBackStack();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
