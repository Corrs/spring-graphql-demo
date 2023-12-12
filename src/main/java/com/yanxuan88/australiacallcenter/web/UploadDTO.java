package com.yanxuan88.australiacallcenter.web;

import lombok.Data;

import javax.servlet.http.Part;
import java.util.List;

@Data
public class UploadDTO {
    private Integer id;
    private List<Part> file;
}
