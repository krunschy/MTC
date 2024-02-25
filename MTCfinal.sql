PGDMP  4                    |            MTCdatabase    16.1    16.1     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16399    MTCdatabase    DATABASE     �   CREATE DATABASE "MTCdatabase" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United Kingdom.1252';
    DROP DATABASE "MTCdatabase";
                postgres    false            �            1259    16405    cards    TABLE     �   CREATE TABLE public.cards (
    cardid text NOT NULL,
    element text,
    cardtype text NOT NULL,
    name text NOT NULL,
    damage bigint NOT NULL,
    creaturetype text
);
    DROP TABLE public.cards;
       public         heap    postgres    false            �            1259    16656    packages    TABLE     �   CREATE TABLE public.packages (
    packageid integer NOT NULL,
    card1 text NOT NULL,
    card2 text NOT NULL,
    card3 text NOT NULL,
    card4 text NOT NULL,
    card5 text NOT NULL
);
    DROP TABLE public.packages;
       public         heap    postgres    false            �            1259    16655    packages_packageID_seq    SEQUENCE     �   CREATE SEQUENCE public."packages_packageID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public."packages_packageID_seq";
       public          postgres    false    220            �           0    0    packages_packageID_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public."packages_packageID_seq" OWNED BY public.packages.packageid;
          public          postgres    false    219            �            1259    16474    stack    TABLE     �   CREATE TABLE public.stack (
    username text NOT NULL,
    cardid text NOT NULL,
    isintradeid text,
    isindeck boolean NOT NULL
);
    DROP TABLE public.stack;
       public         heap    postgres    false            �            1259    16464    trades    TABLE     �   CREATE TABLE public.trades (
    tradeid text NOT NULL,
    cardid text,
    reqdamage integer NOT NULL,
    reqelement text NOT NULL,
    reqtype text NOT NULL
);
    DROP TABLE public.trades;
       public         heap    postgres    false            �            1259    16400    users    TABLE     �   CREATE TABLE public.users (
    username text NOT NULL,
    password text NOT NULL,
    money bigint NOT NULL,
    elo bigint NOT NULL,
    bio text,
    image text,
    wins bigint NOT NULL,
    losses bigint NOT NULL,
    name text
);
    DROP TABLE public.users;
       public         heap    postgres    false            *           2604    16659    packages packageid    DEFAULT     z   ALTER TABLE ONLY public.packages ALTER COLUMN packageid SET DEFAULT nextval('public."packages_packageID_seq"'::regclass);
 A   ALTER TABLE public.packages ALTER COLUMN packageid DROP DEFAULT;
       public          postgres    false    219    220    220            �          0    16405    cards 
   TABLE DATA           V   COPY public.cards (cardid, element, cardtype, name, damage, creaturetype) FROM stdin;
    public          postgres    false    216   a$       �          0    16656    packages 
   TABLE DATA           P   COPY public.packages (packageid, card1, card2, card3, card4, card5) FROM stdin;
    public          postgres    false    220   ^)       �          0    16474    stack 
   TABLE DATA           H   COPY public.stack (username, cardid, isintradeid, isindeck) FROM stdin;
    public          postgres    false    218   �)       �          0    16464    trades 
   TABLE DATA           Q   COPY public.trades (tradeid, cardid, reqdamage, reqelement, reqtype) FROM stdin;
    public          postgres    false    217   �-       �          0    16400    users 
   TABLE DATA           _   COPY public.users (username, password, money, elo, bio, image, wins, losses, name) FROM stdin;
    public          postgres    false    215   �-       �           0    0    packages_packageID_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public."packages_packageID_seq"', 97, true);
          public          postgres    false    219            .           2606    16503    cards Cards_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.cards
    ADD CONSTRAINT "Cards_pkey" PRIMARY KEY (cardid);
 <   ALTER TABLE ONLY public.cards DROP CONSTRAINT "Cards_pkey";
       public            postgres    false    216            4           2606    16663    packages packages_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_pkey PRIMARY KEY (packageid);
 @   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_pkey;
       public            postgres    false    220            2           2606    24593    stack stack_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.stack
    ADD CONSTRAINT stack_pkey PRIMARY KEY (cardid);
 :   ALTER TABLE ONLY public.stack DROP CONSTRAINT stack_pkey;
       public            postgres    false    218            0           2606    16569    trades trades_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_pkey PRIMARY KEY (tradeid);
 <   ALTER TABLE ONLY public.trades DROP CONSTRAINT trades_pkey;
       public            postgres    false    217            ,           2606    16535    users users_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215            9           2606    16664    packages packages_card1_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card1_fkey FOREIGN KEY (card1) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card1_fkey;
       public          postgres    false    216    220    4654            :           2606    16669    packages packages_card2_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card2_fkey FOREIGN KEY (card2) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card2_fkey;
       public          postgres    false    220    216    4654            ;           2606    16674    packages packages_card3_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card3_fkey FOREIGN KEY (card3) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card3_fkey;
       public          postgres    false    4654    216    220            <           2606    16679    packages packages_card4_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.packages
    ADD CONSTRAINT packages_card4_fkey FOREIGN KEY (card4) REFERENCES public.cards(cardid);
 F   ALTER TABLE ONLY public.packages DROP CONSTRAINT packages_card4_fkey;
       public          postgres    false    216    220    4654            6           2606    16623    stack stack_CardID_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.stack
    ADD CONSTRAINT "stack_CardID_fkey" FOREIGN KEY (cardid) REFERENCES public.cards(cardid);
 C   ALTER TABLE ONLY public.stack DROP CONSTRAINT "stack_CardID_fkey";
       public          postgres    false    216    218    4654            7           2606    16634    stack stack_IsInTradeID_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.stack
    ADD CONSTRAINT "stack_IsInTradeID_fkey" FOREIGN KEY (isintradeid) REFERENCES public.trades(tradeid);
 H   ALTER TABLE ONLY public.stack DROP CONSTRAINT "stack_IsInTradeID_fkey";
       public          postgres    false    217    4656    218            8           2606    16608    stack stack_username_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.stack
    ADD CONSTRAINT stack_username_fkey FOREIGN KEY (username) REFERENCES public.users(username);
 C   ALTER TABLE ONLY public.stack DROP CONSTRAINT stack_username_fkey;
       public          postgres    false    4652    218    215            5           2606    16583    trades trades_cardid_fkey    FK CONSTRAINT     {   ALTER TABLE ONLY public.trades
    ADD CONSTRAINT trades_cardid_fkey FOREIGN KEY (cardid) REFERENCES public.cards(cardid);
 C   ALTER TABLE ONLY public.trades DROP CONSTRAINT trades_cardid_fkey;
       public          postgres    false    216    217    4654            �   �  x��VMsd�<k�\$ ��=�.�U�!_��͖UZ�b�7G��T4����M�@���i�JRW"e�ԚnR�)}��������<��<�o�}���rzx9]Z_�6|�[��yRuv���~�ǟQ���?}yz(��t�^��jT��JF�{"�<9��k��L���~||9�9!�%�a}����]h�V2���&�M���_���e�Z�x�6IS4]:��:D�����~��_�.�jS��)�vRK��d��<s�r����v��.�����>i�.���}�&E�{(�7[�5����Q_V�bj��-�xB_tOl��
����։�I��)]K����Ie2�.�H*`�X|�)S�(7/�s�H��]�O��4p�L�X�.uޣc~r%U����@Y�Jn��!Cw}��'ݙ�M9��<&�bӺ]�f-|��ȵ��*ZQ5o���]����1]��5�%hA�(S]�S01Z���R~ڟ~�o׿��&�'���yL���ɞ&�RtGgoCd~L�V"J���
�ꐱ(l�m-家K١6�R6�+�Ә��Fy;$�_�vH�� A�=H�
��Vt��*���6d�L �)d������F�����_ս��s4G�Jb΀S��Y��q���[�s�!;l��} ����Ӯ��]u�l>�3��6��rF[�W�k���nx;t���e��Wo@)i�-u���0�ɥ��k���4���-jn~�#_��`��W�k�@ ��'�5-���C�nŻ��c�!��!����]m/K�DF�J�V��#�_�6�c*�v�8XH��Z�*�j���mW���M�����%5�Rݳ�K�_׶�hI�pp���1��g�<%_	n)�C���'�6a�yh'�LG�m|/�r���f�Dpx�$a��!<�h-�EM����O�?������%qG l#��ʧ����BwF��ĺ��^�
v��2��:�;��?����:��9/�>�:���qS't=
�g��N�;b5���+�-�7(�0��P��H8aCi�����R���mcv�[����mS�
3R���.���n�v��������؟Ŏ؏|�{	�l�##�8�� h�bŞ"�x�@R�H2WH�����g�%�6��l0쏨]���à2L1t��h+6>�w՞�f��u,k����)0E�*u����7,a[k��l�<΀4��%�z�ez��Tr�<�`��V��u�vs��[�7��+�?s�\� �=P}      �   �   x���!�jn灿�K��#�ҩ�tl ��UE��~|/?q����mK@(M`�;R��S4�'�X����2����^j����4���o�K���69��%�@Ӂ6e9Nز�u��ܳ5����<����0�      �   �  x�eUA7<'��c��� �Z�J.��:�$J�ٳg�af��w~9_3>��j7&ɼ��-i-MR��.��ç??|���'d����<�p�A{s�tv�K�/H�HY�h��mFk{#����s��g/k�h��*�ґb�k�E�e��!��o�寯��b�8K��i�IQH�Ɛ��Y�D5��`��K�� 27�:����_㏞�S��%��3ե]�*=����=L:sбT2���8��W/&낢��Q�[��aR�ט�U�cy<Ԏ��4�����������ܸKx4�'��|�M�/O_5�z3v�'6�29Fc�ٮrl}AVB��|�Z�s��}n��*+���@|m<���iL=-a*��������e�R��h���n���E�{����V��p`c�Q�.��AL��C��,���B��4�p�����l?{�B��ܧ-�_�ج���������zk~[���[�}��~��ď}�� ��)٭~��K0������g����	��b�7ߐٖ�R'>�^`~Z7!�0��@/yC�d�Nl�b|F\�.��<��]߂����U��o]Ic+<���zW�V7�8���t��q�UV+����/�[��R��&��@!�����^~��~��[�G´�80%b�5~o���C!����kv����;.�^�v�)Oc+��t��؆�4�_iyZ��0r��TI�S��bC������LA��O@����:��XԐ�J�ܣ�z�9��&O�������i5ǡh�hl�dG�-mq~7��_�6��ɻ"LA�6H�ە�j�CrN��0�Leyl�{�7���ɍ�?�4��mH��sp
d��/֒W^���E�Y`_��i�QG�t��}����i��k�cd��RCd�ds�����˃���P*�������I���9�9��H�O|���_2~,O      �      x������ � �      �   �   x�EϱNC1�9�
V�l'q�nHlH|A'N�S��b��IK��;�s�vY7�9�$��0z�� T�\��
Aj91#���9-<�BK��8pǷ끻&Z�V��\�`�Өe���%ٰ$#E��r� �'3j/6G��o��?|��{ݖeq��Ǜ�zGN^�_}{߇�h��EBT�,��� ���Pb#ѰQEcU-��|\���۟�rӞ�F����{��HS~     