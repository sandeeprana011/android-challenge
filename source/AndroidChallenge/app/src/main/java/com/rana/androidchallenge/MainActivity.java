package com.rana.androidchallenge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   private DownandParse downandParse;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  if (Downloader.isNetwork(this)) {
		 downandParse = new DownandParse();
		 String URL_COMMIT = "https://api.github.com/repos/rails/rails/commits";
		 downandParse.execute(URL_COMMIT);
	  } else {
		 Toast.makeText(this, "Internet Connection not Available.", Toast.LENGTH_LONG)
				 .show();
	  }

   }

   class DownandParse extends AsyncTask<String, Integer, ArrayList<Commit>> {

	  private String stringDownloaded = null;

	  private ArrayList<Commit> commitArrayList;
	  private ProgressBar progressBar;
	  private ListView listView;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();
		 commitArrayList = new ArrayList<>();
		 listView = (ListView) findViewById(R.id.list_commits);
		 progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		 if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		 }
		 if (listView != null) {
			listView.setVisibility(View.GONE);
		 }
	  }

	  @Override
	  protected ArrayList<Commit> doInBackground(String... params) {
		 Downloader downloader = new Downloader();
		 try {
			stringDownloaded = downloader.downloadContent(params[0]);

		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 if (stringDownloaded != null) {
			try {
			   JSONArray jsonArray = new JSONArray(stringDownloaded);
			   if (jsonArray != null) {
				  for (int i = 0; i < jsonArray.length(); i++) {
					 JSONObject comJsonObj = jsonArray.getJSONObject(i); //Json object at index i
					 /**
					  * This block set SHA
					  */
					 Commit commit = new Commit();                     //commit object

					 if (isOk(Commit.N_SHA, comJsonObj)) {
						commit.setCommitSHA(comJsonObj.getString(Commit.N_SHA));
					 } else {
						commit.setCommitSHA("NO Sha Available");
					 }

					 /**
					  * This block of code is for AUTHORNAME & MESSAGE
					  */
					 if (isOk(Commit.N_COMMIT, comJsonObj)) {
						JSONObject commitObject = comJsonObj.getJSONObject(Commit.N_COMMIT);
						if (commitObject != null) {
						   if (isOk(Commit.N_AUTHOR, commitObject)) {
							  JSONObject authorObject = commitObject.getJSONObject(Commit.N_AUTHOR);
							  if (authorObject != null) {
								 if (isOk(Commit.N_AUTHOR_NAME, authorObject)) {
									commit.setCommitAuthorName(authorObject.getString(Commit
											.N_AUTHOR_NAME));
								 } else {
									commit.setCommitAuthorName("No name available");
								 }
							  }

							  if (isOk(Commit.N_COMMIT_MESSAGE, commitObject)) {
								 commit.setCommitMessage(commitObject.getString(Commit.N_COMMIT_MESSAGE));
							  }
						   }
						}
					 }
					 commitArrayList.add(commit);


				  }
			   }
			} catch (JSONException e) {
			   e.printStackTrace();
			}
		 }

		 return commitArrayList;
	  }

	  @Override
	  protected void onPostExecute(ArrayList<Commit> commitArrayList) {
//		 if (!cancel(true)) { //for bet
		 super.onPostExecute(commitArrayList);

		 if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		 }

//		 Log.e("Commits", commitArrayList.get(0).getCommitAuthorName());


		 if (listView != null) {
			listView.setAdapter(new CommitListAdapter(commitArrayList));
			listView.setVisibility(View.VISIBLE);
		 }
//		 }
	  }

	  private boolean isOk(String objName, JSONObject jsonObject) {
		 if (jsonObject.has(objName) && !jsonObject.isNull(objName)) {
			return true;
		 } else return false;
	  }
   }

   class CommitListAdapter extends BaseAdapter {

	  private final ArrayList<Commit> arrayListCommit;

	  public CommitListAdapter(ArrayList<Commit> arrayList) {
		 this.arrayListCommit = arrayList;
	  }

	  @Override
	  public int getCount() {
		 return this.arrayListCommit.size();
	  }

	  @Override
	  public Object getItem(int position) {
		 return arrayListCommit.get(position);
	  }

	  @Override
	  public long getItemId(int position) {
		 return position;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {

		 if (convertView == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getApplicationContext());
			convertView = vi.inflate(R.layout.listitem_commit, null);
		 }

		 Commit commit = arrayListCommit.get(position);

		 TextView textAuthor = (TextView) convertView.findViewById(R.id.author);
		 TextView textSHA = (TextView) convertView.findViewById(R.id.sha);
		 TextView textMessage = (TextView) convertView.findViewById(R.id.message);

		 textAuthor.setText(commit.getCommitAuthorName());
		 textSHA.setText(commit.getCommitSHA());
		 textMessage.setText(commit.getCommitMessage());

		 return convertView;
	  }
   }

   @Override
   protected void onDestroy() {
	  super.onDestroy();
	  if (downandParse != null) {
		 downandParse.cancel(true);
	  }
   }
}
