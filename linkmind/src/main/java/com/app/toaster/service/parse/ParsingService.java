package com.app.toaster.service.parse;

import java.io.IOException;

import com.app.toaster.controller.response.parse.OgResponse;


public interface ParsingService {

	OgResponse getOg(String linkUrl) throws IOException;

}
