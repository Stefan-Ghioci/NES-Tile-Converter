package io.github.stefan_ghioci.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class HierarchicalAgglomerativeClustering<E>
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalAgglomerativeClustering.class);

    public List<List<E>> run(List<E> entities, int desiredClusterCount)
    {
        LOGGER.info("Running complete-linkage HAC algorithm on {} entities into {} clusters ",
                    entities.size(),
                    desiredClusterCount);

        List<List<E>> clusters = initializeClusters(entities);

        int checkpoint = (clusters.size() - desiredClusterCount) / 6;
        int i = 0;
        while (clusters.size() > desiredClusterCount)
        {
            if (i % checkpoint == 0)
                LOGGER.info("{}% complete", (int) ((double) (i) / (i + clusters.size() - desiredClusterCount) * 100));

            List<E> minLinkageCluster1 = clusters.get(0);
            List<E> minLinkageCluster2 = clusters.get(1);

            double minLinkage = completeLinkage(minLinkageCluster1, minLinkageCluster2);

            for (List<E> cluster1 : clusters)
                for (List<E> cluster2 : clusters)
                {
                    if (cluster1 != cluster2)
                    {
                        double linkage = completeLinkage(cluster1, cluster2);

                        if (linkage < minLinkage)
                        {
                            minLinkageCluster1 = cluster1;
                            minLinkageCluster2 = cluster2;
                            minLinkage = linkage;
                        }
                    }
                }

            merge(clusters, minLinkageCluster1, minLinkageCluster2);

            i++;
        }

        LOGGER.info("Clustering complete!");
        return clusters;
    }

    private void merge(List<List<E>> clusters, List<E> minLinkageCluster1, List<E> minLinkageCluster2)
    {
        List<E> newCluster = new ArrayList<>();
        newCluster.addAll(minLinkageCluster1);
        newCluster.addAll(minLinkageCluster2);

        clusters.remove(minLinkageCluster1);
        clusters.remove(minLinkageCluster2);

        clusters.add(newCluster);
    }

    private double completeLinkage(List<E> cluster1, List<E> cluster2)
    {
        double maxDistance = distance(cluster1.get(0), cluster2.get(0));

        for (E entity1 : cluster1)
            for (E entity2 : cluster2)
            {
                double distance = distance(entity1, entity2);
                if (maxDistance < distance)
                    maxDistance = distance;
            }

        return maxDistance;
    }

    protected abstract double distance(E entity1, E entity2);

    private List<List<E>> initializeClusters(List<E> entities)
    {
        return entities.stream()
                       .map(entity -> new ArrayList<>(List.of(entity)))
                       .collect(Collectors.toList());
    }
}
