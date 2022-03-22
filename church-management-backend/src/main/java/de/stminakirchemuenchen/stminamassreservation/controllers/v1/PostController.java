package de.stminakirchemuenchen.stminamassreservation.controllers.v1;

import com.bishoysoliman.stminamassreservation.api.v1.PostApi;
import com.bishoysoliman.stminamassreservation.api.v1.models.PostDTO;
import com.bishoysoliman.stminamassreservation.api.v1.models.PostPage;
import de.stminakirchemuenchen.stminamassreservation.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v1")
public class PostController implements PostApi {

    @Autowired
    private PostService postService;

    public ResponseEntity<PostPage> postsGet(Integer pageIndex, Integer numberOfPosts) {
        return new ResponseEntity<>( new PostPage()
                .posts(this.postService.getPagedPosts(pageIndex,numberOfPosts))
                , HttpStatus.OK);
    }

    public ResponseEntity<PostDTO> postsPost(PostDTO post) {
        return new ResponseEntity<>(this.postService.createPost(post)
                , HttpStatus.OK);
    }
}
