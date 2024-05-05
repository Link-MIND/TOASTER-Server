package com.app.toaster.parse;

import java.io.IOException;

import com.app.toaster.controller.response.parse.OgResponse;
import com.app.toaster.service.parse.ParsingService;

public abstract class ParsingServiceTestImpl implements ParsingService {
	@Override
	public abstract OgResponse getOg(String linkUrl) throws IOException;
}
