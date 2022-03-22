package de.stminakirchemuenchen.stminamassreservation.services;

import com.bishoysoliman.stminamassreservation.api.v1.models.PostDTO;
import de.stminakirchemuenchen.stminamassreservation.entities.Post;
import de.stminakirchemuenchen.stminamassreservation.entities.UserEntity;
import de.stminakirchemuenchen.stminamassreservation.repositories.PostRepository;
import de.stminakirchemuenchen.stminamassreservation.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    public List<PostDTO> getPagedPosts(int pageIndex, int pageSize) {
        return this.postRepository.findAll(
                PageRequest.of(pageIndex,
                        pageSize,
                        Sort.by("creationDate").descending()))
                .stream().map(post ->
                    new PostDTO()
                            .body(post.getText())
                            .uuid(post.getId().toString())
                            .creationDate(post.getCreationDate())
                            .lastUpdatedDate(post.getLastUpdateDate())
                            .createdByName(
                                    post.getCreatedBy().getFirstName()
                                            +" "+post.getCreatedBy().getLastName())
                            .createdByUUID(post.getCreatedBy().getId().toString())
                            .title(post.getTitle())
                ).collect(Collectors.toList());
    }

    public PostDTO createPost(PostDTO postDTO){
        Post post = new Post();
        UserEntity creator = this.userEntityRepository.findByUsername(postDTO.getCreatedByName());
        post.setCreatedBy(creator);
        post.setCreationDate(OffsetDateTime.now());
        post.setLastUpdateDate(post.getCreationDate());
        post.setTitle(postDTO.getTitle());
        post.setText(postDTO.getBody());
        this.postRepository.save(post);
        postDTO.uuid(post.getId().toString())
                .createdByUUID(creator.getId().toString());
        return postDTO;
    }
}
