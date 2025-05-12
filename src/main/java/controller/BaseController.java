package controller;

import model.Request;
import model.Response;

public interface BaseController {
    Response handle(Request request);
}
