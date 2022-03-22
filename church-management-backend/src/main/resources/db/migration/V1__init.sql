--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2 (Debian 13.2-1.pgdg100+1)
-- Dumped by pg_dump version 13.2

-- Started on 2021-04-18 05:52:25 CEST

-- SET statement_timeout = 0;
--SET lock_timeout = 0;
--SET idle_in_transaction_session_timeout = 0;
--SET client_encoding = 'UTF8';
--SET standard_conforming_strings = on;
--SELECT pg_catalog.set_config('search_path', '', false);
--SET check_function_bodies = false;
--SET xmloption = content;
--SET client_min_messages = warning;
--SET row_security = off;
--
--SET default_tablespace = '';
--
--SET default_table_access_method = heap;


--
-- TOC entry 201 (class 1259 OID 16666)
-- Name: liturgie; Type: TABLE; Schema: public; Owner: stminauser
--

CREATE TABLE public.liturgie (
    id uuid NOT NULL,
    church_count integer NOT NULL,
    date timestamp with time zone,
    max_church_count integer NOT NULL,
    max_sala_count integer NOT NULL,
    description character varying(255),
    sala_count integer NOT NULL,
    deleted boolean NOT NULL
);

--
-- TOC entry 202 (class 1259 OID 16671)
-- Name: post; Type: TABLE; Schema: public; Owner: stminauser
--

CREATE TABLE public.post (
    id uuid NOT NULL,
    creation_date timestamp with time zone,
    last_update_date timestamp with time zone,
    text character varying(255),
    title character varying(255),
    created_by_id uuid
);

--
-- TOC entry 203 (class 1259 OID 16679)
-- Name: reservation; Type: TABLE; Schema: public; Owner: stminauser
--

CREATE TABLE public.reservation (
    id uuid NOT NULL,
    approved boolean NOT NULL,
    deleted boolean NOT NULL,
    email character varying(255),
    mobile character varying(255),
    last_name character varying(255),
    first_name character varying(255),
    password character varying(255),
    number_of_people integer NOT NULL,
    place character varying(255),
    registration_date timestamp with time zone,
    liturgie_id uuid
);

--
-- TOC entry 204 (class 1259 OID 16687)
-- Name: user_entity; Type: TABLE; Schema: public; Owner: stminauser
--

CREATE TABLE public.user_entity (
    id uuid NOT NULL,
    email character varying(255),
    enabled boolean NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    username character varying(255)
);

--
-- TOC entry 205 (class 1259 OID 16695)
-- Name: user_entity_roles; Type: TABLE; Schema: public; Owner: stminauser
--

CREATE TABLE public.user_entity_roles (
    user_entity_id uuid NOT NULL,
    roles character varying(255)
);

CREATE TABLE public.user_reservation
(
    id uuid NOT NULL,
    date character varying(255) COLLATE pg_catalog."default",
    place character varying(255) COLLATE pg_catalog."default",
    number_of_places character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default"
);



--
-- TOC entry 2827 (class 2606 OID 16670)
-- Name: liturgie liturgie_pkey; Type: CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.liturgie
    ADD CONSTRAINT liturgie_pkey PRIMARY KEY (id);


--
-- TOC entry 2829 (class 2606 OID 16678)
-- Name: post post_pkey; Type: CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.post
    ADD CONSTRAINT post_pkey PRIMARY KEY (id);


--
-- TOC entry 2831 (class 2606 OID 16686)
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- TOC entry 2833 (class 2606 OID 16694)
-- Name: user_entity user_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT user_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 2834 (class 2606 OID 16698)
-- Name: comment fk_post_user; Type: FK CONSTRAINT; Schema: public; Owner: stminauser
--

--
-- TOC entry 2835 (class 2606 OID 16703)
-- Name: post fk_post_user; Type: FK CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.post
    ADD CONSTRAINT fk_post_user FOREIGN KEY (created_by_id) REFERENCES public.user_entity(id);


--
-- TOC entry 2836 (class 2606 OID 16708)
-- Name: reservation fk_reservation_liturgie; Type: FK CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fk_reservation_liturgie FOREIGN KEY (liturgie_id) REFERENCES public.liturgie(id);


--
-- TOC entry 2837 (class 2606 OID 16713)
-- Name: user_entity_roles fkjvvinok3stf32dvgie3vr73s0; Type: FK CONSTRAINT; Schema: public; Owner: stminauser
--

ALTER TABLE ONLY public.user_entity_roles
    ADD CONSTRAINT fkjvvinok3stf32dvgie3vr73s0 FOREIGN KEY (user_entity_id) REFERENCES public.user_entity(id);


CREATE TABLE public.comment
(
    id uuid NOT NULL,
    creation_date timestamp with time zone,
    last_update_date timestamp with time zone,
    text character varying(255) COLLATE pg_catalog."default",
    created_by_id uuid,
    post_comments_id uuid,
    CONSTRAINT comment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_post_comment FOREIGN KEY (post_comments_id)
        REFERENCES public.post (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_post_user FOREIGN KEY (created_by_id)
        REFERENCES public.user_entity (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- Completed on 2021-04-18 05:52:26 CEST

--
-- PostgreSQL database dump complete
--

