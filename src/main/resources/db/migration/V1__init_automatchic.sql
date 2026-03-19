--
-- PostgreSQL database dump
--

-- Dumped from database version 18.2
-- Dumped by pg_dump version 18.2

-- Started on 2026-03-17 12:37:43

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- TOC entry 225 (class 1259 OID 25061)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 25040)
-- Name: subject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subject (
    id bigint NOT NULL,
    name character varying NOT NULL,
    teacher character varying,
    description character varying,
    grading_type character varying,
    grading_max double precision,
    grading_5 double precision,
    grading_4 double precision,
    grading_3 double precision,
    grading_min double precision,
    target_grade integer,
    publicity character varying,
    user_id bigint NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 25039)
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
-- TOC entry 224 (class 1259 OID 25051)
-- Name: task; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.task (
    id bigint NOT NULL,
    name character varying NOT NULL,
    type character varying,
    due_date date,
    max_grade double precision,
    received_grade double precision,
    grade_weight double precision,
    subject_id bigint NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 25050)
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
-- TOC entry 220 (class 1259 OID 25029)
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    full_name character varying,
    "group" character varying,
    role character varying NOT NULL
);


--
-- TOC entry 219 (class 1259 OID 25028)
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
-- TOC entry 5034 (class 0 OID 25061)
-- Dependencies: 225
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	<< Flyway Baseline >>	BASELINE	<< Flyway Baseline >>	\N	postgres	2026-03-15 14:06:18.39081	0	t
\.


--
-- TOC entry 5031 (class 0 OID 25040)
-- Dependencies: 222
-- Data for Name: subject; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.subject (id, name, teacher, description, grading_type, grading_max, grading_5, grading_4, grading_3, grading_min, target_grade, user_id, publicity) FROM stdin;
\.


--
-- TOC entry 5033 (class 0 OID 25051)
-- Dependencies: 224
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.task (id, name, type, due_date, max_grade, received_grade, grade_weight, subject_id) FROM stdin;
\.


--
-- TOC entry 5029 (class 0 OID 25029)
-- Dependencies: 220
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public."user" (id, username, password, full_name, "group", role) FROM stdin;
\.


--
-- TOC entry 5040 (class 0 OID 0)
-- Dependencies: 221
-- Name: subject_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.subject_id_seq', 1, false);


--
-- TOC entry 5041 (class 0 OID 0)
-- Dependencies: 223
-- Name: task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.task_id_seq', 1, false);


--
-- TOC entry 5042 (class 0 OID 0)
-- Dependencies: 219
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_id_seq', 1, false);


--
-- TOC entry 4879 (class 2606 OID 25076)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 4875 (class 2606 OID 25049)
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- TOC entry 4877 (class 2606 OID 25060)
-- Name: task task_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- TOC entry 4873 (class 2606 OID 25038)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 4880 (class 1259 OID 25077)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


-- Completed on 2026-03-17 12:37:43

--
-- PostgreSQL database dump complete
--
