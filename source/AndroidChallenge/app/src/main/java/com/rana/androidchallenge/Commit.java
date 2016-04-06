package com.rana.androidchallenge;

/**
 * Created by sandeeprana on 06/04/16.
 * This class contains important fields we required.
 *
 */
public class Commit {
   public static final String N_SHA="sha"; // roor>
   public static final String N_COMMIT_MESSAGE="message";
   public static final String N_AUTHOR="author";
   public static final String N_COMMIT="commit";
   public static final String N_AUTHOR_NAME="name";

   private String commitSHA;
   private String commitMessage;
   private String commitAuthorName;

   public String getCommitSHA() {
	  return commitSHA;
   }

   public void setCommitSHA(String commitSHA) {
	  this.commitSHA = commitSHA;
   }

   public String getCommitMessage() {
	  return commitMessage;
   }

   public void setCommitMessage(String commitMessage) {
	  this.commitMessage = commitMessage;
   }

   public String getCommitAuthorName() {
	  return commitAuthorName;
   }

   public void setCommitAuthorName(String commitAuthorName) {
	  this.commitAuthorName = commitAuthorName;
   }
}
