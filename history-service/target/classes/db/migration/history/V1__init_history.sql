--
-- PostgreSQL database dump
--

-- Dumped from database version 18.2
-- Dumped by pg_dump version 18.2

-- Started on 2026-04-20 00:32:11

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
-- TOC entry 219 (class 1259 OID 41998)
-- Name: progress_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.progress_history (
    id bigint NOT NULL,
    subject_id bigint NOT NULL,
    score_delta double precision,
    weight_delta double precision,
    grading_type character varying,
    evaluation_type character varying,
    target_grade integer,
    grading_max double precision,
    grading_5 double precision,
    grading_4 double precision,
    grading_3 double precision,
    grading_min double precision,
    event_type character varying,
    "timestamp" timestamp without time zone
);


ALTER TABLE public.progress_history OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 42027)
-- Name: progress_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.progress_history ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.progress_history_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 221 (class 1259 OID 42028)
-- Name: progress_view; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.progress_view (
    subject_id bigint NOT NULL,
    total_score double precision DEFAULT 0 NOT NULL,
    total_weight double precision DEFAULT 0 NOT NULL,
    grading_type character varying DEFAULT 'GRADE'::character varying NOT NULL,
    evaluation_type character varying DEFAULT 'TOTAL'::character varying NOT NULL,
    target_grade integer DEFAULT 1 NOT NULL,
    grading_max double precision DEFAULT 5 NOT NULL,
    grading_5 double precision DEFAULT 5 NOT NULL,
    grading_4 double precision DEFAULT 4 NOT NULL,
    grading_3 double precision DEFAULT 3 NOT NULL,
    grading_min double precision DEFAULT 0 NOT NULL
);


ALTER TABLE public.progress_view OWNER TO postgres;

--
-- TOC entry 5031 (class 0 OID 41998)
-- Dependencies: 219
-- Data for Name: progress_history; Type: TABLE DATA; Schema: public; Owner: postgres
--


SELECT pg_catalog.setval('public.progress_history_id_seq', 1, false);


--
-- TOC entry 4881 (class 2606 OID 42055)
-- Name: progress_history progress_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.progress_history
    ADD CONSTRAINT progress_history_pkey PRIMARY KEY (id);


--
-- TOC entry 4883 (class 2606 OID 42057)
-- Name: progress_view progress_view_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.progress_view
    ADD CONSTRAINT progress_view_pkey PRIMARY KEY (subject_id);


-- Completed on 2026-04-20 00:32:11

--
-- PostgreSQL database dump complete
--

