package org.njctl.courseapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.material.*;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MaterialsFragment extends ListFragment implements TwoStatesDecider<Document>
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.materials_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		MaterialsActivity selector = (MaterialsActivity) getActivity();
		Unit unit = selector.getUnit();

		ListView docView = (ListView) getView().findViewById(android.R.id.list);

		ArrayList<Document> data = new ArrayList<Document>();
		data.addAll(unit.getHomeworks());
		data.addAll(unit.getLabs());

		for (Presentation pres : unit.getPresentations()) {
			if (pres.hasTopics()) {
				for (Topic topic : pres.getTopics()) {
					data.add(topic);
				}
			} else {
				data.add(pres);
			}
		}
		data.addAll(unit.getHandouts());

		TwoStatesAdapter<Document> adapter = new TwoStatesAdapter<Document>(getActivity(), data, this);
		docView.setAdapter(adapter);
		//docView.setAdapter(new ArrayAdapter<Document>(getActivity(), android.R.layout.simple_list_item_activated_1,
		//		data));

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
			{
				Document doc = (Document) adapter.getItemAtPosition(position);
				Log.v("NJCTLDocSelect", "Selected doc " + doc.getName());

				openPDF(doc);
			}
		});
	}

	protected void openPDF(Document doc)
	{
		try {
			String filePath = doc.getAbsolutePathForOpening();
			
			FileInputStream docStream = new FileInputStream(filePath);
			//BufferedInputStream docStream = new BufferedInputStream(getActivity().openFileInput(filePath));
			// create a buffer that has the same size as the InputStream
			byte[] buffer = new byte[docStream.available()];
			// read the text file as a stream, into the buffer
			docStream.read(buffer);
			// create a output stream to write the buffer into
			// TODO: Context.MODE_WORLD_READABLE is deprecated. Look into
			// replacing this with a ContentProvider.
			BufferedOutputStream outStream = new BufferedOutputStream(getActivity().openFileOutput(doc.getFileName(),
					Context.MODE_WORLD_READABLE));
			// write this buffer to the output stream
			outStream.write(buffer);
			// Close the Input and Output streams
			outStream.close();
			docStream.close();

			// Try to launch the PDF in a PDF viewer.
			Intent intent = new Intent(Intent.ACTION_VIEW);
			File docFile = new File(getActivity().getFilesDir(), doc.getFileName());
			docFile.setReadable(true, false);
			docFile.setWritable(true, false); // So that people can take notes
												// in the PDF reader.
			Uri docUri = Uri.fromFile(docFile);
			intent.setDataAndType(docUri, doc.getMIMEType());
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// Show an error message if the user does not have an appropriate application for opening the document.
			Log.w("ERROR", e.toString());
			Log.v("NJCTLLOG", "Error: No activity found for viewing MIME type " + doc.getMIMEType() + ".");
		} catch (FileNotFoundException e) {
			// Show an error message if the file isn't there.
			Log.w("ERROR", e.toString());
			Log.v("NJCTLLOG", "Error: Could not open file / file not found.");
			Log.v("NJCTLLOG", Log.getStackTraceString(e));
		} catch (IOException e) {
			Log.w("ERROR", e.toString());
			Log.v("NJCTLLOG", Log.getStackTraceString(e));
		}
	}

	@Override
	public boolean isActive(Document content)
	{
		return true;
	}
}
