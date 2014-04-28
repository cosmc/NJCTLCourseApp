package org.njctl.courseapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.util.Log;

public class Model implements AsyncJsonResponse
{
	final String NJCTLLOG = "NJCTL";
	protected SubjectRetriever retriever;
	
	public void fetchManifest(SubjectRetriever retrieverObject)
	{
		retriever = retrieverObject;
		
		new RetrieveManifestTask().execute(this);
	}
/*
	public ArrayList<NJCTLClass> getClassTree(String relativePath,
			Resources resources)
	{
		// Build all of the necessary NJCTLClass objects from the JSON course
		// manifest file.
		StringBuilder text = new StringBuilder();
		BufferedReader br = null;
		String line;

		// Build a StringBuilder from the manifest file.
		try {
			br = new BufferedReader(new InputStreamReader(resources.getAssets()
					.open(relativePath)));
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			// do exception handling
			Log.w("ERR", e.toString());
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				Log.w("ERR", e.toString());
			}
		}

		// Now build the actual class tree!
		// TODO: Okay this is questionable. We probably should have some kind of
		// recursive datatype like "NJCTLContainer" or something.
		// For now, though, the rules:
		// - The top level JSON object has a JSON array called "classes".
		// - Each element of "classes" has a string "id" and a JSON array
		// "chapters".
		// - Each element of each "chapters" has a string "id" and a JSON array
		// "doclists".
		// - Each element of each "doclists" has a string "id" and a JSON array
		// "documents".
		// - Each element of each "documents" is a string indicating the
		// filename.
		// - The relative path from the assets directory to each document is
		// courses/($class_id)/($chapter_id)/($doclist_id)/[filename]
		//
		// The TITLE of an NJCTLCLass is the same as its ID. The TITLE of a
		// chapter or doclist is equal to id.split("\\.")[1].
		// The manifest builder script will not include chapters or doclists
		// whose IDs do not include a "."

		ArrayList<NJCTLClass> njctlClasses = new ArrayList<NJCTLClass>(); // We'll
																			// spend
																			// the
																			// rest
																			// of
																			// this
																			// method
																			// filling
																			// this
																			// dude,
																			// and
																			// then
																			// return
																			// him.

		try {

			JSONObject courseManifest = new JSONObject(text.toString()); // Build
																			// the
																			// course
																			// manifest
																			// as
																			// a
																			// JSON
																			// object
																			// from
																			// the
																			// StringBuilder.
			JSONArray classes = courseManifest.getJSONArray("classes"); // Get
																		// the
																		// list
																		// of
																		// class
																		// objects.

			for (int i = 0; i < classes.length(); ++i) {
				JSONObject currentClass = classes.getJSONObject(i);
				JSONArray chapters = currentClass.getJSONArray("chapters");
				NJCTLClass klass = new NJCTLClass(currentClass.getString("id"));
				njctlClasses.add(klass);

				for (int j = 0; j < chapters.length(); ++j) {
					JSONObject currentChapter = chapters.getJSONObject(j);
					JSONArray docLists = currentChapter
							.getJSONArray("doclists");

					Unit chapter = new Unit(
							currentChapter.getString("id"), currentChapter
									.getString("id").split("\\.")[1]);
					klass.add(chapter);

					for (int k = 0; k < docLists.length(); ++k) {
						JSONObject currentDocList = docLists.getJSONObject(k);
						JSONArray docs = currentDocList
								.getJSONArray("documents");

						NJCTLDocList docList = new NJCTLDocList(
								currentDocList.getString("id"), currentDocList
										.getString("id").split("\\.")[1]);
						chapter.add(docList);

						for (int l = 0; l < docs.length(); ++l) {
							// Construct the path to the document from the
							// assets folder.
							String pathToDoc = "courses/"
									+ currentClass.getString("id") + "/"
									+ currentChapter.getString("id") + "/"
									+ currentDocList.getString("id") + "/"
									+ docs.getString(l);

							Document doc = new Document(docs.getString(l));
							doc.setPath(pathToDoc);

							docList.add(doc);
						}
					}
				}
			}
		} catch (JSONException e) {
			Log.w("JSON ERR", e.toString());
		}

		return njctlClasses; // Now I can't look up from my screen without
								// seeing curly braces everywhere.

	}*/

	@Override
	public void processJson(JSONObject json)
	{
		/*
		ArrayList<NJCTLClass> classes = new ArrayList<NJCTLClass>();
		
		try {
			JSONArray results = json.getJSONArray("pages");
			
			for(int i = 0; i < results.length(); i++)
			{
				NJCTLClass klass = new NJCTLClass(results.getJSONObject(i));
				classes.add(klass);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}*/
		
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		retriever.useSubjects(subjects);
	}
}
