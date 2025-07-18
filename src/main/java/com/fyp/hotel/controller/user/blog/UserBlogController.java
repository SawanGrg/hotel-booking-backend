package com.fyp.hotel.controller.user.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.dto.blog.BlogCommentDTO;
import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.blog.BlogDTO;
import com.fyp.hotel.dto.user.UserProfileDto;
import com.fyp.hotel.service.user.UserServiceFacade;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@Validated
public class UserBlogController {

    private UserServiceFacade userServiceFacade;
    private UserProfileDto userProfileDto;
    private ValueMapper valueMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public UserBlogController(UserServiceFacade userServiceFacade, UserProfileDto userProfileDto, ValueMapper valueMapper, ObjectMapper objectMapper) {
        this.userServiceFacade = userServiceFacade;
        this.userProfileDto = userProfileDto;
        this.valueMapper = valueMapper;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/postBlog")
    public ResponseEntity<?> postBlog(
            @RequestParam("blogImage") MultipartFile blogImage,
            @RequestParam("blogDTO") String blogDTOJson
    ) throws JsonProcessingException {
        BlogDTO blogDTO = objectMapper.readValue(blogDTOJson, BlogDTO.class);
        String response = this.userServiceFacade.userBlogService.postUserBlog(blogImage, blogDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
    @GetMapping("/viewBlog")
    public ResponseEntity<?> viewBlog() {
        List<BlogDTO> blogs = this.userServiceFacade.userBlogService.viewUserBlog();
        ApiResponse<List<BlogDTO>> response = new ApiResponse<>(200, "Success", blogs);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/viewBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(
            @PathVariable(name = "blogId") long blogId
    ) {
        BlogDTO blog = this.userServiceFacade.userBlogService.getSpecificBlog(blogId);
        ApiResponse<BlogDTO> response = new ApiResponse<>(200, "Success", blog);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/postBlogComment/{blogId}")
    public ResponseEntity<?> postBlogComment(
            @PathVariable(name = "blogId") long blogId,
            @RequestBody BlogCommentDTO blogCommentDTO
    ) {
        String response = this.userServiceFacade.userBlogService.postBlogComment(blogId, blogCommentDTO);
        ApiResponse<String> successResponse = new ApiResponse<>(200, "Success", response);
        return ResponseEntity.status(200).body(successResponse);
    }

    @GetMapping("/viewPostBlogComment/{blogId}")
    public ResponseEntity<?> viewBlogComment(
            @PathVariable(name = "blogId") long blogId
    ) {
        List<BlogCommentDTO> blogCommentDTOs = this.userServiceFacade.userBlogService.getAllBlogCommentSpecificBlog(blogId);
        ApiResponse<List<BlogCommentDTO>> blogCommentResponse = new ApiResponse<>(200, "success", blogCommentDTOs);
        return ResponseEntity.status(200).body(blogCommentResponse);
    }
}
