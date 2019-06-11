# IT
no
#include<iostream>
#include<vector>
#include<stack>
#include<queue>
#include<iterator>
using namespace std;

const int INF = 999999;     // 表示无穷大（此例中）

template<class T> class Vertex;             // 提前声明顶点类

template<class T>
class InfoTable {       // 创建一个信息表类
public:
    bool Known;         // 是否被遍历
    int dist;           // 顶点间的距离
    T Path;     // 用顶点关键字表示的路径栏
};

template<class T>
class Vertex {      // 创建一个顶点类
public:
    T value;        // 顶点的关键字值
    vector<Vertex<T>> adj_list;     // 顶点的邻接表
    InfoTable<T>* table;        // 最短路径时每个顶点的信息栏
    int weight;     // 顶点之间的权重（相邻顶点的连接权值），存放在邻接顶点中，每个顶点与自身的权值为0
    Vertex(T value = 0) :value(value), weight(0) {} // 默认构造函数
};

template<class T>
class Graph {       // 创建一个图类
public:
    vector<Vertex<T>> VSet;     // 表示顶点的集合
    Graph(int sz) :size(sz) {}  // 构造函数
    Graph(const Graph<T> &G) { size = G.size;VSet = G.VSet; }   // 复制构造函数

    void InitInfoTable();       // 初始化图中顶点的状态信息表

    int UnknownMinDistVertex(int index);    // 找index状态信息表中未知的（Known=0）最小dist顶点（的下标）
    void Dijkstra(int index);       // Dijkstra算法求某个顶点(图中下标为index的顶点)的有权最短路径
    void PrintPath(int index);  // 打印某个节点的最短路径

private:
    int size;                   // 图中顶点的个数
};

template<class T>
void Graph<T>::InitInfoTable()      // 初始化图中顶点的状态信息表
{
    for (int i = 0;i < size;i++)
    {
        VSet[i].table = new InfoTable<T>[size]; // 为每个顶点的状态表申请空间
        for (int j = 0;j < size;j++)
        {
            VSet[i].table[j].Known = false;     // 每个节点都没被经过
            VSet[i].table[j].dist = INF;        // 初始时每个顶点距离为无穷，表示不可达
            VSet[i].table[j].Path = -1;
        }
        VSet[i].table[i].dist = 0;              // 初始时每个顶点距离自身为0
    }
}

template<class T>
int Graph<T>::UnknownMinDistVertex(int index)
{
    int MinIndex = -1;  // 初始化未知最小dist顶点下标为-1
    for (int i = 0;i < size;i++)        
    {
        if (!VSet[index].table[i].Known)        // 首先找到第一个未知的顶点
            MinIndex = i;                       // 如果未知点（未被声明已知的顶点）存在，则更新MinIndex的值
    }

    for (int i = 0;i < size;i++)                // 再一次遍历index顶点的状态信息表
    {
        // 当某个顶点的dist小于当前最小dist值且未知
        if (VSet[index].table[i].dist < VSet[index].table[MinIndex].dist && !VSet[index].table[i].Known)        
            MinIndex = i;               // 更新最小dist值下标
    }
    return MinIndex;
}

template<class T>
void Graph<T>::Dijkstra(int index)      // Dijkstra算法实现，目的是为了改变index顶点的状态信息表
{
    Vertex<T> W;
    int MinIndex;
    while (1)
    {
        MinIndex = UnknownMinDistVertex(index);     // 寻找当前index顶点的状态信息表的最小dist值的顶点下标
        if (MinIndex == -1)     // 当MinIndex为-1，表示所有点都被标明已知，结束循环
            break;  
        VSet[index].table[MinIndex].Known = true;       // 标记最小dist值的顶点为已知
        // 遍历当前最小dist值的顶点的邻接表
        for (vector<Vertex<T>>::iterator iter = VSet[MinIndex].adj_list.begin(); iter != VSet[MinIndex].adj_list.end();iter++)
        {
            if (!VSet[index].table[iter->value].Known)  // 如果其邻接点未被声明已知，则
                if (VSet[index].table[MinIndex].dist + iter->weight < VSet[index].table[iter->value].dist)  
                {   // 如果最小dist顶点dist加上该顶点与其邻接点的连接权值小于其邻接点原有的dist值，则更新其邻接点的dist值
                    VSet[index].table[iter->value].dist = VSet[index].table[MinIndex].dist + iter->weight;
                    VSet[index].table[iter->value].Path = VSet[MinIndex].value;     // 更新最小dist顶点的邻接点的Path值为最小dist顶点（的值，为了方便）
                }
        }
    }
}

template<class T>
void Graph<T>::PrintPath(int index)
{
    cout << "The InfoTable of V" << index << " is:\n";
    cout << "Vertex Known   dist    Path" << endl;
    for (int i = 0;i < size;i++)    // 打印下标为index的顶点的状态表
    {
        cout << "V" << i << "\t" << VSet[index].table[i].Known << "\t" << VSet[index].table[i].dist
            << "\t" << "V" << VSet[index].table[i].Path << endl;
    }
    cout << "\nShow the weighted shortest paths from V" << index << " to other vertices by Dijkstra Alogrithm: \n";
    stack<T> S;         // 借助栈输出从index顶点出发到各个顶点的无权最短路径
    for (int i = 0;i < size;i++)
    {
        if (i == index)
            continue;
        if (VSet[index].table[i].dist == INF)
        {
            cout << "Unreachable!\n";
            continue;
        }
        cout << "The shortest path from V" << index << " to V" << i << " is "
            << VSet[index].table[i].dist<<" long, and the path is: ";
        int j = i;      // 每次取一个顶点作为开始
        S.push(VSet[j].value);  // 顶点入栈
        while (VSet[index].table[j].Path != -1) // 当遍历指定顶点的“Path”形成路径
        {
            S.push(VSet[index].table[j].Path);  // 将路径上的顶点的Path值入栈
            j = VSet[index].table[j].Path;      // 更新顶点下标
        }
        while (!S.empty())                      // 栈不为空时，打印路径并出栈
        {
            if(S.top() != index)
                cout << "->";
            cout << "V" << S.top();
            S.pop();
        }
        cout << endl;
    }
    cout << endl;
}

int main()
{
    Graph<int> G(7);            // 创建一个图对象G
    Vertex<int> V[] = { Vertex<int>(0), Vertex<int>(1), Vertex<int>(2), Vertex<int>(3),
        Vertex<int>(4), Vertex<int>(5), Vertex<int>(6) };

    V[0].adj_list.push_back(V[1]);  // 顶点V0的邻接表
    V[0].adj_list.push_back(V[3]);  
    V[0].adj_list[0].weight = 2;    // V0与V1的连接权值
    V[0].adj_list[1].weight = 1;    // V0与V3的连接权值

    V[1].adj_list.push_back(V[3]);  // 顶点V1的邻接表
    V[1].adj_list.push_back(V[4]);
    V[1].adj_list[0].weight = 3;    // V1与V2的连接权值
    V[1].adj_list[1].weight = 10;   // V1与V4的连接权值

    V[2].adj_list.push_back(V[0]);  // 顶点V2的邻接表
    V[2].adj_list.push_back(V[5]);  
    V[2].adj_list[0].weight = 4;    // V2与V0的连接权值
    V[2].adj_list[1].weight = 5;    // V2与V5的连接权值

    V[3].adj_list.push_back(V[2]);  // 顶点V3的邻接表
    V[3].adj_list.push_back(V[4]);
    V[3].adj_list.push_back(V[5]);
    V[3].adj_list.push_back(V[6]);
    V[3].adj_list[0].weight = 2;    // V3与V2的连接权值
    V[3].adj_list[1].weight = 2;    // V3与V4的连接权值
    V[3].adj_list[2].weight = 8;    // V3与V5的连接权值
    V[3].adj_list[3].weight = 4;    // V3与V6的连接权值

    V[4].adj_list.push_back(V[6]);  // 顶点V4的邻接表
    V[4].adj_list[0].weight = 6;    // V4与V6的连接权值

    V[6].adj_list.push_back(V[5]);  // 顶点V6的邻接表
    V[6].adj_list[0].weight = 1;    // V6与V5的连接权值

    for (int i = 0;i < 7;i++)
    {
        G.VSet.push_back(V[i]);
    }
    G.InitInfoTable();      // 初始化图中顶点的状态信息表
    G.Dijkstra(0);          // Dijkstra算法求G中顶点V2到其他顶点的有权最短路径
    G.PrintPath(0);         // 打印从V2到其他顶点路径

    system("pause");
    return 0;
}
--------------------- 
作者：SanFanCSgo 
来源：CSDN 
原文：https://blog.csdn.net/weixin_40170902/article/details/80794062 
版权声明：本文为博主原创文章，转载请附上博文链接！
