h = figure;
graphData = zeros(11,2);
for i = 0:10
    fname = ['embryo_' num2str(i) '_']
    raw_data = readFile(fname);
    cell = findMeshData(raw_data{2,1}.components);
    graphData(i+1,1) = cell(1).position.x;
    graphData(i+1,2) = cell(1).position.y;   
    
end

plot(graphData(:,1), graphData(:,2));

function data = findMeshData(struct)
    for i = 1:length(fieldnames(struct))
        if(struct(i).type == 'CellMesh')
            data = struct(i).properties.nodes;
            break;
        end
    end
end

function raw_data = readFile(name)
    fname = 'embryo_5_.txt'
    fid = fopen(fname)
    raw = fread(fid,inf);
    str = char(raw');
    fclose(fid);
    raw_data = jsondecode(str);
end