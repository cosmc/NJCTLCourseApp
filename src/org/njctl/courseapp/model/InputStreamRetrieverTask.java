package org.njctl.courseapp.model;

import java.io.InputStream;

public class InputStreamRetrieverTask extends FileRetrieverTask<InputStream>
{
	@Override
	InputStream convertContents(InputStream inputStream)
	{
		return inputStream;
	}
}