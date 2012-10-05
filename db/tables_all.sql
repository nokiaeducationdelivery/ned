--
-- PostgreSQL database dump
--

-- Dumped from database version 9.0.3
-- Dumped by pg_dump version 9.0.3
-- Started on 2011-06-02 09:52:00

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 1859 (class 1262 OID 98365)
-- Name: ned; Type: DATABASE; Schema: -; Owner: ned
--

ALTER DATABASE ned OWNER TO ned;


SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 333 (class 2612 OID 11574)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: ned
--

CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO ned;

SET search_path = public, pg_catalog;

--
-- TOC entry 18 (class 1255 OID 98366)
-- Dependencies: 6 333
-- Name: change_version_on_delete(); Type: FUNCTION; Schema: public; Owner: ned
--

CREATE FUNCTION change_version_on_delete() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
	par text;
	currentId text;
BEGIN 
	IF OLD.parentid != 'NULL' THEN
	
		currentId := OLD."elementid";
		RAISE NOTICE 'currentID: %', currentId;
		
		SELECT INTO par "getRootId"(currentId); 
		RAISE NOTICE 'parent: %', par;
		
		UPDATE containers SET version = version + 1 WHERE elementid = par;
	END IF;
	
	return OLD;
END
$$;


ALTER FUNCTION public.change_version_on_delete() OWNER TO ned;

--
-- TOC entry 19 (class 1255 OID 98367)
-- Dependencies: 333 6
-- Name: change_version_on_update_insert(); Type: FUNCTION; Schema: public; Owner: ned
--

CREATE FUNCTION change_version_on_update_insert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
	par text;
	currentId text;
BEGIN 
	IF ((TG_OP != 'UPDATE') OR (NEW."version" = OLD."version")) THEN
		currentId := NEW."elementid";	
	
		SELECT INTO par "getRootId"(currentId); 
		UPDATE containers SET version = version + 1 WHERE elementid = par;
	END IF;
	return NEW;
END
$$;


ALTER FUNCTION public.change_version_on_update_insert() OWNER TO ned;

--
-- TOC entry 20 (class 1255 OID 98368)
-- Dependencies: 6 333
-- Name: getRootId(text); Type: FUNCTION; Schema: public; Owner: ned
--

CREATE FUNCTION "getRootId"("objectId" text) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE 
	parent text;
BEGIN
	SELECT INTO parent parentid FROM containers WHERE  elementId LIKE "objectId";

	IF parent IS NULL
	THEN
		return "objectId";
	ELSE
		return "getRootId"(parent);
	END IF;

END;
$$;


ALTER FUNCTION public."getRootId"("objectId" text) OWNER TO ned;

--
-- TOC entry 21 (class 1255 OID 98369)
-- Dependencies: 6 333
-- Name: set_user_roles_on_insert(); Type: FUNCTION; Schema: public; Owner: ned
--

CREATE FUNCTION set_user_roles_on_insert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
	name text;
BEGIN 
		INSERT INTO userroles (role, name)  VALUES ('user', NEW.name);
	return NEW;
END$$;


ALTER FUNCTION public.set_user_roles_on_insert() OWNER TO ned;

--
-- TOC entry 22 (class 1255 OID 98370)
-- Dependencies: 333 6
-- Name: update_version(); Type: FUNCTION; Schema: public; Owner: ned
--

CREATE FUNCTION update_version() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
	par text;
	currentId text;
BEGIN 
	IF ((TG_OP != 'UPDATE') OR (NEW."version" = OLD."version")) THEN

		IF((TG_OP = 'DELETE')) THEN
			currentId := OLD."elementid";
		ELSE 
			currentId := NEW."elementid";
		END IF;
		RAISE NOTICE 'currentID: %', currentId;
		
		SELECT INTO par "getRootId"(currentId); 
		UPDATE containers SET version = version + 1 WHERE elementid = par;
	END IF;
	return NEW;
END
$$;


ALTER FUNCTION public.update_version() OWNER TO ned;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1525 (class 1259 OID 98371)
-- Dependencies: 1814 6
-- Name: containers; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE containers (
    id integer NOT NULL,
    parentid character varying(40),
    name character varying(40) NOT NULL,
    type character varying(40) NOT NULL,
    data text,
    elementid character varying(40),
    description text,
    keywords character varying(15)[],
    links character varying(150)[],
    version integer DEFAULT 0
);


ALTER TABLE public.containers OWNER TO ned;

--
-- TOC entry 1526 (class 1259 OID 98378)
-- Dependencies: 6 1525
-- Name: containers_id_seq; Type: SEQUENCE; Schema: public; Owner: ned
--

CREATE SEQUENCE containers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.containers_id_seq OWNER TO ned;

--
-- TOC entry 1862 (class 0 OID 0)
-- Dependencies: 1526
-- Name: containers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ned
--

ALTER SEQUENCE containers_id_seq OWNED BY containers.id;


--
-- TOC entry 1863 (class 0 OID 0)
-- Dependencies: 1526
-- Name: containers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ned
--

SELECT pg_catalog.setval('containers_id_seq', 180, true);


--
-- TOC entry 1527 (class 1259 OID 98380)
-- Dependencies: 6
-- Name: containertypes; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE containertypes (
    name character varying(40) NOT NULL
);


ALTER TABLE public.containertypes OWNER TO ned;

--
-- TOC entry 1528 (class 1259 OID 98383)
-- Dependencies: 6
-- Name: extensions; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE extensions (
    extension character(10) NOT NULL,
    type character varying(20)
);


ALTER TABLE public.extensions OWNER TO ned;


CREATE TABLE languages (
    id integer NOT NULL,
    locale_string character varying(10) NOT NULL,
    name character varying(25) NOT NULL,
    translation_file text NOT NULL,
    font_file text
);

ALTER TABLE public.languages OWNER TO ned;

CREATE SEQUENCE languages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.languages_id_seq OWNER TO ned;

ALTER SEQUENCE languages_id_seq OWNED BY languages.id;

SELECT pg_catalog.setval('languages_id_seq', 1, false);

CREATE TABLE motd (
    motd_id integer NOT NULL,
    message text
);


ALTER TABLE public.motd OWNER TO ned;

--
-- TOC entry 1530 (class 1259 OID 98392)
-- Dependencies: 6
-- Name: statistics; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE statistics (
    id integer NOT NULL,
    username character varying(40),
    deviceid character varying(20),
    type character varying,
    details character varying,
    "time" character varying
);


ALTER TABLE public.statistics OWNER TO ned;

--
-- TOC entry 1531 (class 1259 OID 98398)
-- Dependencies: 6 1530
-- Name: statistics_id_seq; Type: SEQUENCE; Schema: public; Owner: ned
--

CREATE SEQUENCE statistics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.statistics_id_seq OWNER TO ned;

--
-- TOC entry 1864 (class 0 OID 0)
-- Dependencies: 1531
-- Name: statistics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ned
--

ALTER SEQUENCE statistics_id_seq OWNED BY statistics.id;


--
-- TOC entry 1865 (class 0 OID 0)
-- Dependencies: 1531
-- Name: statistics_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ned
--

SELECT pg_catalog.setval('statistics_id_seq', 811, true);


--
-- TOC entry 1532 (class 1259 OID 98400)
-- Dependencies: 6
-- Name: statistictypes; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE statistictypes (
    name character varying(40) NOT NULL
);


ALTER TABLE public.statistictypes OWNER TO ned;

--
-- TOC entry 1533 (class 1259 OID 98403)
-- Dependencies: 6
-- Name: userroles; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE userroles (
    role character varying(40) NOT NULL,
    name character varying(40),
    id integer NOT NULL
);


ALTER TABLE public.userroles OWNER TO ned;

--
-- TOC entry 1534 (class 1259 OID 98406)
-- Dependencies: 1533 6
-- Name: userroles_id_seq; Type: SEQUENCE; Schema: public; Owner: ned
--

CREATE SEQUENCE userroles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.userroles_id_seq OWNER TO ned;

--
-- TOC entry 1866 (class 0 OID 0)
-- Dependencies: 1534
-- Name: userroles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ned
--

ALTER SEQUENCE userroles_id_seq OWNED BY userroles.id;


--
-- TOC entry 1867 (class 0 OID 0)
-- Dependencies: 1534
-- Name: userroles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ned
--

SELECT pg_catalog.setval('userroles_id_seq', 13, true);


--
-- TOC entry 1535 (class 1259 OID 98408)
-- Dependencies: 1818 1819 6
-- Name: users; Type: TABLE; Schema: public; Owner: ned; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    password character varying(40) NOT NULL,
    lastlogin timestamp without time zone DEFAULT now(),
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.users OWNER TO ned;

--
-- TOC entry 1536 (class 1259 OID 98413)
-- Dependencies: 6 1535
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: ned
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO ned;

--
-- TOC entry 1868 (class 0 OID 0)
-- Dependencies: 1536
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ned
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 1869 (class 0 OID 0)
-- Dependencies: 1536
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ned
--

SELECT pg_catalog.setval('users_id_seq', 22, true);


--
-- TOC entry 1815 (class 2604 OID 98415)
-- Dependencies: 1526 1525
-- Name: id; Type: DEFAULT; Schema: public; Owner: ned
--

ALTER TABLE containers ALTER COLUMN id SET DEFAULT nextval('containers_id_seq'::regclass);

ALTER TABLE languages ALTER COLUMN id SET DEFAULT nextval('languages_id_seq'::regclass);

--
-- TOC entry 1816 (class 2604 OID 98416)
-- Dependencies: 1531 1530
-- Name: id; Type: DEFAULT; Schema: public; Owner: ned
--

ALTER TABLE statistics ALTER COLUMN id SET DEFAULT nextval('statistics_id_seq'::regclass);


--
-- TOC entry 1817 (class 2604 OID 98417)
-- Dependencies: 1534 1533
-- Name: id; Type: DEFAULT; Schema: public; Owner: ned
--

ALTER TABLE userroles ALTER COLUMN id SET DEFAULT nextval('userroles_id_seq'::regclass);


--
-- TOC entry 1820 (class 2604 OID 98418)
-- Dependencies: 1536 1535
-- Name: id; Type: DEFAULT; Schema: public; Owner: ned
--

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 1850 (class 0 OID 98380)
-- Dependencies: 1527
-- Data for Name: containertypes; Type: TABLE DATA; Schema: public; Owner: ned
--

INSERT INTO containertypes VALUES ('Library');
INSERT INTO containertypes VALUES ('Category');
INSERT INTO containertypes VALUES ('Catalog');
INSERT INTO containertypes VALUES ('Picture');
INSERT INTO containertypes VALUES ('Undefined');
INSERT INTO containertypes VALUES ('Video');
INSERT INTO containertypes VALUES ('Audio');
INSERT INTO containertypes VALUES ('Text');


--
-- TOC entry 1851 (class 0 OID 98383)
-- Dependencies: 1528
-- Data for Name: extensions; Type: TABLE DATA; Schema: public; Owner: ned
--

INSERT INTO extensions VALUES ('mp3', 'Audio');
INSERT INTO extensions VALUES ('mp4', 'Video');
INSERT INTO extensions VALUES ('3gp', 'Video');
INSERT INTO extensions VALUES ('', 'Undefined');
INSERT INTO extensions VALUES ('txt', 'Text');
INSERT INTO extensions VALUES ('jpeg', 'Picture');
INSERT INTO extensions VALUES ('jpg', 'Picture');
INSERT INTO extensions VALUES ('png', 'Picture');
INSERT INTO extensions VALUES ('wav', 'Audio');
INSERT INTO extensions VALUES ('pdf', 'Text');
INSERT INTO extensions VALUES ('rtf', 'Text');


--
-- TOC entry 1854 (class 0 OID 98400)
-- Dependencies: 1532
-- Data for Name: statistictypes; Type: TABLE DATA; Schema: public; Owner: ned
--

INSERT INTO statistictypes VALUES ('BROWSE_LIBRARY_OPEN');
INSERT INTO statistictypes VALUES ('BROWSE_LIBRARY_BACK');
INSERT INTO statistictypes VALUES ('BROWSE_CATALOG_OPEN');
INSERT INTO statistictypes VALUES ('BROWSE_CATALOG_BACK');
INSERT INTO statistictypes VALUES ('BROWSE_CATEGORY_OPEN');
INSERT INTO statistictypes VALUES ('BROWSE_CATEGORY_BACK');
INSERT INTO statistictypes VALUES ('BROWSE_MEDIAITEM_BACK');
INSERT INTO statistictypes VALUES ('DOWNLOAD_ADD');
INSERT INTO statistictypes VALUES ('DOWNLOAD_REMOVE');
INSERT INTO statistictypes VALUES ('DOWNLOAD_START');
INSERT INTO statistictypes VALUES ('DOWNLOAD_END');
INSERT INTO statistictypes VALUES ('DOWNLOAD_COMPLETED');
INSERT INTO statistictypes VALUES ('LIBRARY_ADD');
INSERT INTO statistictypes VALUES ('LIBRARY_REMOVED');
INSERT INTO statistictypes VALUES ('PLAY_ITEM_START');
INSERT INTO statistictypes VALUES ('PLAY_ITEM_END');
INSERT INTO statistictypes VALUES ('LINK_OPEN');
INSERT INTO statistictypes VALUES ('DETAILS_SHOW');
INSERT INTO statistictypes VALUES ('SHOW_LINKS');
INSERT INTO statistictypes VALUES ('DELETE_ITEM');
INSERT INTO statistictypes VALUES ('SEARCH_ITEM');
INSERT INTO statistictypes VALUES ('DELETE_HISTORY_ITEM');
INSERT INTO statistictypes VALUES ('PLAY_HISTORY_ITEM');
INSERT INTO statistictypes VALUES ('USER_LOGGED');
INSERT INTO statistictypes VALUES ('USER_DELETE');
INSERT INTO statistictypes VALUES ('APP_EXIT');


--
-- TOC entry 1855 (class 0 OID 98403)
-- Dependencies: 1533
-- Data for Name: userroles; Type: TABLE DATA; Schema: public; Owner: ned
--

INSERT INTO userroles VALUES ('admin', 'admin', 1);


--
-- TOC entry 1856 (class 0 OID 98408)
-- Dependencies: 1535
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: ned
--
INSERT INTO users VALUES (2, 'admin', 'text2teach', '2011-05-04 21:45:11.562', true);


--
-- TOC entry 1823 (class 2606 OID 98420)
-- Dependencies: 1525 1525
-- Name: containers_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY containers
    ADD CONSTRAINT containers_pkey PRIMARY KEY (id);


--
-- TOC entry 1825 (class 2606 OID 98422)
-- Dependencies: 1527 1527
-- Name: containertypes_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY containertypes
    ADD CONSTRAINT containertypes_pkey PRIMARY KEY (name);


--
-- TOC entry 1827 (class 2606 OID 98424)
-- Dependencies: 1528 1528
-- Name: extensions_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY extensions
    ADD CONSTRAINT extensions_pkey PRIMARY KEY (extension);

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (id);

--
-- TOC entry 1829 (class 2606 OID 98426)
-- Dependencies: 1529 1529
-- Name: motd_id_key; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY motd
    ADD CONSTRAINT motd_id_key PRIMARY KEY (motd_id);


--
-- TOC entry 1831 (class 2606 OID 98428)
-- Dependencies: 1530 1530
-- Name: statistics_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY statistics
    ADD CONSTRAINT statistics_pkey PRIMARY KEY (id);


--
-- TOC entry 1833 (class 2606 OID 98430)
-- Dependencies: 1532 1532
-- Name: statistictypes_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY statistictypes
    ADD CONSTRAINT statistictypes_pkey PRIMARY KEY (name);


--
-- TOC entry 1835 (class 2606 OID 98432)
-- Dependencies: 1533 1533
-- Name: userroles_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY userroles
    ADD CONSTRAINT userroles_pkey PRIMARY KEY (id);


--
-- TOC entry 1837 (class 2606 OID 98434)
-- Dependencies: 1535 1535
-- Name: users_name_key; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_name_key UNIQUE (name);


--
-- TOC entry 1839 (class 2606 OID 98436)
-- Dependencies: 1535 1535
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: ned; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 1821 (class 1259 OID 98437)
-- Dependencies: 1525
-- Name: containers_element_id; Type: INDEX; Schema: public; Owner: ned; Tablespace: 
--

CREATE UNIQUE INDEX containers_element_id ON containers USING btree (elementid);


--
-- TOC entry 1846 (class 2620 OID 98438)
-- Dependencies: 1525 18
-- Name: change_ver_delete; Type: TRIGGER; Schema: public; Owner: ned
--

CREATE TRIGGER change_ver_delete BEFORE DELETE ON containers FOR EACH ROW EXECUTE PROCEDURE change_version_on_delete();


--
-- TOC entry 1847 (class 2620 OID 98439)
-- Dependencies: 1525 19
-- Name: change_ver_update_insert; Type: TRIGGER; Schema: public; Owner: ned
--

CREATE TRIGGER change_ver_update_insert AFTER INSERT OR UPDATE ON containers FOR EACH ROW EXECUTE PROCEDURE change_version_on_update_insert();


--
-- TOC entry 1848 (class 2620 OID 98440)
-- Dependencies: 1535 21
-- Name: set_role; Type: TRIGGER; Schema: public; Owner: ned
--

CREATE TRIGGER set_role AFTER INSERT ON users FOR EACH ROW EXECUTE PROCEDURE set_user_roles_on_insert();


--
-- TOC entry 1840 (class 2606 OID 98441)
-- Dependencies: 1525 1525 1821
-- Name: containers_parentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ned
--

ALTER TABLE ONLY containers
    ADD CONSTRAINT containers_parentid_fkey FOREIGN KEY (parentid) REFERENCES containers(elementid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1841 (class 2606 OID 98446)
-- Dependencies: 1824 1525 1527
-- Name: containers_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ned
--

ALTER TABLE ONLY containers
    ADD CONSTRAINT containers_type_fkey FOREIGN KEY (type) REFERENCES containertypes(name) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 1842 (class 2606 OID 98451)
-- Dependencies: 1528 1527 1824
-- Name: media_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ned
--

ALTER TABLE ONLY extensions
    ADD CONSTRAINT media_type_fkey FOREIGN KEY (type) REFERENCES containertypes(name);


--
-- TOC entry 1843 (class 2606 OID 98456)
-- Dependencies: 1530 1532 1832
-- Name: statistics_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ned
--

ALTER TABLE ONLY statistics
    ADD CONSTRAINT statistics_type_fkey FOREIGN KEY (type) REFERENCES statistictypes(name) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1844 (class 2606 OID 98461)
-- Dependencies: 1836 1530 1535
-- Name: statistics_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ned
--

ALTER TABLE ONLY statistics
    ADD CONSTRAINT statistics_username_fkey FOREIGN KEY (username) REFERENCES users(name) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1845 (class 2606 OID 98466)
-- Dependencies: 1535 1836 1533
-- Name: userroles_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: ned
--

ALTER TABLE ONLY userroles
    ADD CONSTRAINT userroles_user_fkey FOREIGN KEY (name) REFERENCES users(name) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1861 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2011-06-02 09:52:01

--
-- PostgreSQL database dump complete
--

