fname = 'embryo_5_.txt'
fid = fopen(fname)
raw = fread(fid,inf);
str = char(raw');
fclose(fid);
val = jsondecode(str);

graphData = zeros(0,11);
for i = 2:81
   data = findMeshData(val{i,1}.components);
    
    
end

function data = findMeshData(struct)
    for i = 1:length(fieldnames(struct))
        if(struct(i).type == 'CellMesh')
            data = struct(i).properties.nodes;
            break;
        end
    end
end