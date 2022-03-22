<script>
    import { onMount } from "svelte";
    import Api from "../../Api/Api";
    onMount(async () => {
        getPosts();
    });

    let posts = [];

    async function getPosts() {
        Api.Post.get(0, 10)
            .then((response) => {
                posts = [...posts, ...response.data.posts];
                console.log(posts);
            })
            .catch((error) => {
                console.log(error);
            });
    }
</script>

<h1 class="title level-item">Neuigkeiten</h1>
<div class="columns">
    {#each posts as post (post.uuid)}
        <div class="column is-10 center">
            <div class="card">
                <header class="card-header">
                    <p class="card-header-title">
                        {post.title}
                    </p>
                </header>
                <div class="card-content">
                    <div class="content">
                        {post.body}
                    </div>
                </div>
            </div>
        </div>
    {/each}
</div>
