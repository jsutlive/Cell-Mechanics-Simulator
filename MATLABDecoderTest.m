fname = 'embryo.txt'
fid = fopen(fname)
raw = fread(fid,inf);
str = char(raw');
fclose(fid);
val = jsondecode(str);