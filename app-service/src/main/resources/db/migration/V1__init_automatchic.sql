--
-- PostgreSQL database dump
--

-- Dumped from database version 18.2
-- Dumped by pg_dump version 18.2

-- Started on 2026-03-25 12:15:09

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


--
-- TOC entry 227 (class 1259 OID 25250)
-- Name: link; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.link (
    id bigint NOT NULL,
    name character varying DEFAULT 'Название'::character varying NOT NULL,
    full_link character varying DEFAULT 'https://example.com/'::character varying NOT NULL,
    subject_id bigint NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 25249)
-- Name: link_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.link ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.link_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 25212)
-- Name: subject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subject (
    id bigint NOT NULL,
    name character varying DEFAULT 'Название'::character varying NOT NULL,
    teacher character varying DEFAULT ''::character varying,
    description character varying DEFAULT ''::character varying,
    grading_type character varying DEFAULT 'EXAM'::character varying NOT NULL,
    grading_max double precision DEFAULT 100,
    grading_5 double precision DEFAULT 80,
    grading_4 double precision DEFAULT 60,
    grading_3 double precision DEFAULT 40,
    grading_min double precision DEFAULT 20,
    target_grade integer DEFAULT 0 NOT NULL,
    publicity character varying DEFAULT 'PUBLIC'::character varying NOT NULL,
    user_id bigint NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 25220)
-- Name: subject_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.subject ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.subject_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 25221)
-- Name: task; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.task (
    id bigint NOT NULL,
    name character varying DEFAULT 'Название'::character varying NOT NULL,
    type character varying DEFAULT 'HOMEWORK'::character varying,
    due_date timestamp without time zone DEFAULT '2000-01-01'::date,
    max_grade double precision DEFAULT 5,
    received_grade double precision DEFAULT 0,
    grade_weight double precision DEFAULT 1,
    "position" integer DEFAULT 0 NOT NULL,
    subject_id bigint NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 25229)
-- Name: task_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.task ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.task_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 25230)
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    full_name character varying DEFAULT ''::character varying,
    "group" character varying DEFAULT ''::character varying,
    role character varying DEFAULT 'STUDENT'::character varying NOT NULL,
    registered_at timestamp without time zone NOT NULL,
    is_confirmed boolean DEFAULT false NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 25239)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public."user" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);

--
-- TOC entry 5071 (class 0 OID 0)
-- Dependencies: 226
-- Name: link_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.link_id_seq', 1, false);


--
-- TOC entry 5072 (class 0 OID 0)
-- Dependencies: 221
-- Name: subject_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.subject_id_seq', 1, false);


--
-- TOC entry 5073 (class 0 OID 0)
-- Dependencies: 223
-- Name: task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.task_id_seq', 1, false);


--
-- TOC entry 5074 (class 0 OID 0)
-- Dependencies: 225
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_id_seq', 1, false);


--
-- TOC entry 4909 (class 2606 OID 25260)
-- Name: link link_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.link
    ADD CONSTRAINT link_pkey PRIMARY KEY (id);


--
-- TOC entry 4903 (class 2606 OID 25243)
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- TOC entry 4905 (class 2606 OID 25245)
-- Name: task task_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- TOC entry 4907 (class 2606 OID 25247)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


-- Completed on 2026-03-25 12:15:09

--
-- PostgreSQL database dump complete
--

